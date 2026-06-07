package com.javastudy.security;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class SecureCodingDemo {

    private static final Pattern USERNAME = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]{2,31}");
    private static final Set<String> BLOCKED_HOSTS = Set.of("localhost", "127.0.0.1", "0.0.0.0");

    public boolean validUsername(String value) {
        return value != null && USERNAME.matcher(value).matches();
    }

    public String preparedSqlTemplate() {
        return "select * from users where email = ?";
    }

    public String escapeHtml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    public boolean allowedOutboundUrl(String url) {
        URI uri = URI.create(url);
        String scheme = uri.getScheme();
        String host = uri.getHost();
        return ("https".equalsIgnoreCase(scheme) || "http".equalsIgnoreCase(scheme))
                && host != null
                && !BLOCKED_HOSTS.contains(host.toLowerCase());
    }

    public String maskSecret(String value) {
        if (value == null || value.length() <= 4) {
            return "****";
        }
        return "*".repeat(value.length() - 4) + value.substring(value.length() - 4);
    }

    public String sanitizeForLog(String value) {
        return value.replace('\n', '_').replace('\r', '_');
    }

    public List<String> dependencyVulnerabilityChecklist() {
        return List.of("pin versions", "scan CVE", "remove unused dependencies", "upgrade JDK");
    }

    public List<String> deserializationDefenses() {
        return List.of("avoid native serialization", "use ObjectInputFilter", "allow-list classes");
    }

    public List<String> immutableCopy(List<String> input) {
        return List.copyOf(input);
    }

    public Optional<String> optionalEmail(String value) {
        return value == null || value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    public List<String> layeredArchitecture() {
        return List.of("controller", "service", "repository", "domain");
    }
}
