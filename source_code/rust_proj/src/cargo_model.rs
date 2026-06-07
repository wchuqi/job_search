//! Small Cargo engineering models for package targets, SemVer, feature
//! unification, resolver selection, MSRV, lockfiles, commands, and profiles.
//!
//! Test scenarios cover version parsing failures, caret/exact matching,
//! resolver defaults, feature merging, command classification, and profile
//! behavior without depending on external crates.

use std::collections::BTreeSet;

#[derive(Debug, Clone, Copy, PartialEq, Eq, PartialOrd, Ord)]
pub struct Version {
    pub major: u64,
    pub minor: u64,
    pub patch: u64,
}

impl Version {
    pub fn parse(input: &str) -> Option<Self> {
        // 只支持 `major.minor.patch` 这种最小模型。
        let mut parts = input.split('.');
        // `?` 遇到 None 会让整个函数返回 None。
        let major = parts.next()?.parse().ok()?;
        let minor = parts.next()?.parse().ok()?;
        let patch = parts.next()?.parse().ok()?;
        // 如果还有第四段，说明格式不符合预期。
        if parts.next().is_some() {
            None
        } else {
            Some(Self {
                major,
                minor,
                patch,
            })
        }
    }
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum VersionReq {
    Caret(Version),
    Exact(Version),
}

impl VersionReq {
    pub fn matches(self, candidate: Version) -> bool {
        // 这里模拟 Cargo 常见的 exact 和 caret 版本匹配规则。
        match self {
            VersionReq::Exact(required) => required == candidate,
            VersionReq::Caret(required) => {
                // ^1.2.3 允许 <2.0.0；^0.2.3 允许 <0.3.0。
                let upper = if required.major > 0 {
                    Version {
                        major: required.major + 1,
                        minor: 0,
                        patch: 0,
                    }
                } else if required.minor > 0 {
                    Version {
                        major: 0,
                        minor: required.minor + 1,
                        patch: 0,
                    }
                } else {
                    Version {
                        major: 0,
                        minor: 0,
                        patch: required.patch + 1,
                    }
                };
                candidate >= required && candidate < upper
            }
        }
    }
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum PackageKind {
    Application,
    Library,
}

pub fn should_commit_lockfile(kind: PackageKind) -> bool {
    // 应用通常提交 Cargo.lock；库是否提交取决于团队策略。
    matches!(kind, PackageKind::Application)
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum Edition {
    E2018,
    E2021,
    E2024,
}

pub fn default_resolver(edition: Edition) -> u8 {
    // Rust 2024 默认 resolver 3；本项目用模型表达，不要求本机支持 2024。
    match edition {
        Edition::E2018 | Edition::E2021 => 2,
        Edition::E2024 => 3,
    }
}

pub fn msrv_satisfies(current: Version, required: Version) -> bool {
    // 当前编译器版本不低于 MSRV 即满足要求。
    current >= required
}

pub fn unify_features(feature_sets: &[&[&str]]) -> BTreeSet<String> {
    // BTreeSet 会去重并排序，适合演示 feature unification。
    feature_sets
        .iter()
        .flat_map(|set| set.iter().copied())
        .map(str::to_string)
        .collect()
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum TargetKind {
    Lib,
    Bin,
    Test,
    Bench,
}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct CargoTarget {
    pub name: String,
    pub kind: TargetKind,
}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct Package {
    pub name: String,
    pub targets: Vec<CargoTarget>,
}

pub fn package_summary(package: &Package) -> String {
    // 这里只输出包名和 target 数量，模拟 cargo metadata 摘要。
    let target_count = package.targets.len();
    format!("{}:{target_count}", package.name)
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum CommandCategory {
    Build,
    Test,
    Quality,
    Metadata,
}

pub fn command_category(command: &str) -> Option<CommandCategory> {
    // 把常见 cargo 命令归类，未知命令返回 None。
    match command {
        "cargo build" | "cargo run" => Some(CommandCategory::Build),
        "cargo test" => Some(CommandCategory::Test),
        "cargo fmt" | "cargo clippy" => Some(CommandCategory::Quality),
        "cargo tree" | "cargo metadata" => Some(CommandCategory::Metadata),
        _ => None,
    }
}

#[derive(Debug, Clone, PartialEq, Eq)]
pub struct Profile {
    pub name: &'static str,
    pub optimized: bool,
    pub debug_assertions: bool,
}

pub fn profile(name: &str) -> Option<Profile> {
    // dev 和 release 是 Cargo 最常见的两个 profile。
    match name {
        "dev" => Some(Profile {
            name: "dev",
            optimized: false,
            debug_assertions: true,
        }),
        "release" => Some(Profile {
            name: "release",
            optimized: true,
            debug_assertions: false,
        }),
        _ => None,
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn v(input: &str) -> Version {
        Version::parse(input).unwrap()
    }

    #[test]
    fn parses_versions_and_models_semver_caret_ranges() {
        // 这个测试覆盖合法版本、非法版本、caret 和 exact 匹配。
        assert_eq!(
            Version::parse("1.2.3"),
            Some(Version {
                major: 1,
                minor: 2,
                patch: 3
            })
        );
        assert_eq!(Version::parse("1.2"), None);
        assert!(VersionReq::Caret(v("1.2.3")).matches(v("1.9.0")));
        assert!(!VersionReq::Caret(v("1.2.3")).matches(v("2.0.0")));
        assert!(VersionReq::Caret(v("0.2.3")).matches(v("0.2.9")));
        assert!(!VersionReq::Caret(v("0.2.3")).matches(v("0.3.0")));
        assert!(VersionReq::Exact(v("1.2.3")).matches(v("1.2.3")));
        assert!(!VersionReq::Exact(v("1.2.3")).matches(v("1.2.4")));
    }

    #[test]
    fn covers_workspace_lockfile_resolver_features_and_msrv() {
        // 这些断言覆盖 lockfile 策略、resolver、MSRV 和 feature 合并。
        assert!(should_commit_lockfile(PackageKind::Application));
        assert!(!should_commit_lockfile(PackageKind::Library));
        assert_eq!(default_resolver(Edition::E2018), 2);
        assert_eq!(default_resolver(Edition::E2021), 2);
        assert_eq!(default_resolver(Edition::E2024), 3);
        assert!(msrv_satisfies(v("1.78.0"), v("1.70.0")));
        assert!(!msrv_satisfies(v("1.60.0"), v("1.70.0")));
        let features = unify_features(&[&["serde", "std"], &["std", "derive"]]);
        assert_eq!(
            features.into_iter().collect::<Vec<_>>(),
            vec!["derive", "serde", "std"]
        );
    }

    #[test]
    fn covers_targets_commands_and_profiles() {
        // 构造一个同时包含 lib/bin/test/bench target 的 package。
        let package = Package {
            name: "demo".into(),
            targets: vec![
                CargoTarget {
                    name: "demo".into(),
                    kind: TargetKind::Lib,
                },
                CargoTarget {
                    name: "demo-cli".into(),
                    kind: TargetKind::Bin,
                },
                CargoTarget {
                    name: "api".into(),
                    kind: TargetKind::Test,
                },
                CargoTarget {
                    name: "speed".into(),
                    kind: TargetKind::Bench,
                },
            ],
        };
        assert_eq!(package_summary(&package), "demo:4");
        assert_eq!(command_category("cargo test"), Some(CommandCategory::Test));
        assert_eq!(
            command_category("cargo clippy"),
            Some(CommandCategory::Quality)
        );
        assert_eq!(
            command_category("cargo tree"),
            Some(CommandCategory::Metadata)
        );
        assert_eq!(command_category("cargo run"), Some(CommandCategory::Build));
        assert_eq!(command_category("cargo publish"), None);
        assert!(profile("dev").unwrap().debug_assertions);
        assert!(profile("release").unwrap().optimized);
        assert_eq!(profile("custom"), None);
    }
}
