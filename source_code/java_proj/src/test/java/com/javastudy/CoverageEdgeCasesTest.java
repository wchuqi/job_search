package com.javastudy;

import com.javastudy.basics.ControlFlowDemo;
import com.javastudy.basics.DivisionDemo;
import com.javastudy.basics.IntegerOverflowDemo;
import com.javastudy.basics.MethodDemo;
import com.javastudy.basics.NumericPromotionDemo;
import com.javastudy.basics.PrimitiveTypesDemo;
import com.javastudy.basics.SwitchExpressionDemo;
import com.javastudy.collections.ArrayListDemo;
import com.javastudy.collections.CapacityDemo;
import com.javastudy.collections.ComparableDemo;
import com.javastudy.collections.ComparatorDemo;
import com.javastudy.collections.HashSetDemo;
import com.javastudy.collections.ImmutableCollectionsDemo;
import com.javastudy.collections.IteratorDemo;
import com.javastudy.collections.SequencedCollectionsDemo;
import com.javastudy.collections.TreeSetDemo;
import com.javastudy.concurrency.ConcurrencyDemo;
import com.javastudy.exception.DuplicateLoggingDemo;
import com.javastudy.functional.MethodReferenceDemo;
import com.javastudy.functional.ReduceDemo;
import com.javastudy.functional.StreamBasicsDemo;
import com.javastudy.functional.StreamSideEffectDemo;
import com.javastudy.generics.AnnotationDemo;
import com.javastudy.generics.Box;
import com.javastudy.generics.OrderStatus;
import com.javastudy.generics.PatternMatchingDemo;
import com.javastudy.io.NioChannelBufferDemo;
import com.javastudy.io.SocketDemo;
import com.javastudy.jdbc.JdbcPersistenceDemo;
import com.javastudy.oop.Account;
import com.javastudy.oop.Animal;
import com.javastudy.oop.Circle;
import com.javastudy.oop.OverrideDemo;
import com.javastudy.oop.Product;
import com.javastudy.oop.Rectangle;
import com.javastudy.oop.User;
import com.javastudy.oop.UserId;
import com.javastudy.stringdatetime.ArraysDemo;
import com.javastudy.stringdatetime.BigDecimalComparisonDemo;
import com.javastudy.stringdatetime.InstantZonedDateTimeDemo;
import com.javastudy.stringdatetime.LocalDateDemo;
import com.javastudy.stringdatetime.ObjectsDemo;
import com.javastudy.stringdatetime.RandomDemo;
import com.javastudy.web.WebSpringDemo;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoverageEdgeCasesTest {

    @Test
    void basicsEdgeCases() {
        assertEquals(2, ControlFlowDemo.sumUntilNegative(new int[]{1, 2, -1}));
        assertThrows(ArithmeticException.class, () -> DivisionDemo.divideByZero(1));
        assertEquals(6, IntegerOverflowDemo.safeMultiply(2, 3));
        assertTrue(IntegerOverflowDemo.willOverflow(Integer.MIN_VALUE, -1));
        assertDoesNotThrow(MethodDemo::doNothing);
        assertEquals(3, NumericPromotionDemo.addShorts((short) 1, (short) 2));
        assertEquals(1, PrimitiveTypesDemo.getByteSize("boolean"));
        assertThrows(IllegalArgumentException.class, () -> SwitchExpressionDemo.daysInMonth("Bad", 2024));
        assertThrows(IllegalArgumentException.class, () -> SwitchExpressionDemo.seasonFromMonth(13));
    }

    @Test
    void collectionsEdgeCases() {
        ArrayListDemo arrayListDemo = new ArrayListDemo();
        assertThrows(RuntimeException.class, () -> arrayListDemo.getInternalCapacity(new LinkedList<>()));

        CapacityDemo capacityDemo = new CapacityDemo();
        assertEquals(2, capacityDemo.defaultHashMap(2).size());

        ComparableDemo.Employee employee = new ComparableDemo.Employee("A", 10);
        assertEquals(employee.hashCode(), new ComparableDemo.Employee("A", 10).hashCode());
        assertEquals(10, employee.getSalary());

        HashSetDemo.Point point = new HashSetDemo.Point(1, 2);
        assertEquals(1, point.getX());
        assertEquals(2, point.getY());

        assertEquals(List.of("Pen", "Book", "Phone", "Laptop", "Tablet"), new ComparatorDemo().multiFieldSort());
        assertEquals(List.of("A", "C", "[B]"), new SequencedCollectionsDemo().pollFirstLast());

        ImmutableCollectionsDemo immutable = new ImmutableCollectionsDemo();
        assertTrue(immutable.tryAddToList(new java.util.ArrayList<>(List.of("A"))));
        assertFalse(immutable.tryAddToList(List.of("A")));
        assertFalse(immutable.tryAddNull());

        assertTrue(new IteratorDemo().failFastDemo());

        TreeSetDemo.Student student = new TreeSetDemo.Student("A", 90);
        assertTrue(student.equals(student));
        assertTrue(student.equals(new TreeSetDemo.Student("A", 90)));
        assertFalse(student.equals("A"));
        assertEquals(student.hashCode(), new TreeSetDemo.Student("A", 90).hashCode());
        assertEquals(90, student.getGrade());
    }

    @Test
    void exceptionFunctionalAndGenericsEdgeCases() {
        DuplicateLoggingDemo.clearLog();
        assertThrows(RuntimeException.class, DuplicateLoggingDemo::controllerLayer);
        assertEquals(3, DuplicateLoggingDemo.getLogEntries().size());

        MethodReferenceDemo.Person person = MethodReferenceDemo.createPerson("Ada", 20);
        assertTrue(person.isAdult());
        assertEquals("Ada(20)", person.toString());

        assertEquals(List.of("A", "B", "C", "D", "E", "F", "G", "H"),
                ReduceDemo.toUpperCaseViaReduce(List.of("a", "b", "c", "d", "e", "f", "g", "h")));
        assertTrue(StreamBasicsDemo.demonstrateSingleUse() instanceof IllegalStateException);
        assertEquals(List.of("a!", "b!").toString(),
                StreamSideEffectDemo.badMutateInMap(List.of("a", "b")).toString());

        AnnotationDemo annotationDemo = new AnnotationDemo();
        assertEquals("Order created: 1", annotationDemo.createOrder("1"));
        assertDoesNotThrow(() -> annotationDemo.deleteOrder("1"));
        assertEquals("no audit", annotationDemo.unannotatedMethod());
        assertEquals("Box{value=x}", new Box<>("x").toString());
        assertEquals("已支付", OrderStatus.PAID.getDescription());
        assertEquals("已发货", OrderStatus.SHIPPED.getDescription());
        assertEquals("已送达", OrderStatus.DELIVERED.getDescription());
        assertEquals("Long: 1", PatternMatchingDemo.switchPatternMatch(1L));
        assertEquals("Double: 1.0", PatternMatchingDemo.switchPatternMatch(1.0));
        assertEquals("Other: Object", PatternMatchingDemo.switchPatternMatch(new Object()));
    }

    @Test
    void ioJdbcAndConcurrencyEdgeCases() throws Exception {
        var invalidUtf8 = Files.createTempFile("bad-utf8", ".bin");
        Files.write(invalidUtf8, new byte[]{(byte) 0xC3, 0x28});
        assertThrows(IllegalArgumentException.class, () -> NioChannelBufferDemo.readWithChannel(invalidUtf8));

        try (ServerSocket server = new ServerSocket(0);
             var executor = Executors.newSingleThreadExecutor()) {
            var future = executor.submit(() -> {
                try (Socket socket = server.accept()) {
                    SocketDemo.handleClient(socket);
                }
                return null;
            });
            try (Socket client = new Socket("localhost", server.getLocalPort());
                 var out = new PrintWriter(client.getOutputStream(), true);
                 var in = new java.io.BufferedReader(new java.io.InputStreamReader(client.getInputStream()))) {
                out.println("echo");
                assertEquals("echo", in.readLine());
            }
            future.get();
        }

        try (ServerSocket server = new ServerSocket(0);
             var executor = Executors.newSingleThreadExecutor()) {
            var future = executor.submit(() -> SocketDemo.acceptAndEcho(server));
            try (Socket ignored = new Socket("localhost", server.getLocalPort())) {
                ignored.shutdownOutput();
            }
            assertNull(future.get());
        }

        JdbcPersistenceDemo jdbc = new JdbcPersistenceDemo();
        DataSource dataSource = h2();
        jdbc.createSchema(dataSource);
        assertThrows(Exception.class, () -> {
            try (var connection = dataSource.getConnection();
                 var statement = connection.createStatement()) {
                statement.executeUpdate("drop table account");
            }
            jdbc.transfer(dataSource, 1, 2, 10);
        });

        assertEquals(List.of("message"), new ConcurrencyDemo().lockConditionExchange("message"));
        Class<?> mailboxType = Class.forName("com.javastudy.concurrency.ConcurrencyDemo$Mailbox");
        var constructor = mailboxType.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object mailbox = constructor.newInstance();
        var take = mailboxType.getDeclaredMethod("take");
        var put = mailboxType.getDeclaredMethod("put", String.class);
        take.setAccessible(true);
        put.setAccessible(true);
        try (var executor = Executors.newSingleThreadExecutor()) {
            var future = executor.submit(() -> {
                try {
                    return take.invoke(mailbox);
                } catch (ReflectiveOperationException ex) {
                    throw new RuntimeException(ex);
                }
            });
            Thread.sleep(50);
            put.invoke(mailbox, "waited");
            assertEquals("waited", future.get());
        }
    }

    @Test
    void oopStringDateAndWebEdgeCases() throws Exception {
        Account account = new Account(10);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
        assertEquals("dog", new Animal("dog").getName());
        assertEquals(2, new Circle(2).getRadius());
        assertEquals(3, new Rectangle(3, 4).getWidth());
        assertEquals(4, new Rectangle(3, 4).getHeight());

        OverrideDemo override = new OverrideDemo("x", 1);
        assertEquals("x", override.getName());
        assertEquals(1, override.getValue());
        assertFalse(override.equals("x"));

        Product product = new Product("p", 1.25);
        assertEquals("Product{name='p', price=1.25}", product.toString());

        User user = new User("A", 1);
        assertThrows(IllegalArgumentException.class, () -> user.rename(" "));
        assertDoesNotThrow(() -> user.rename("B"));
        assertThrows(IllegalArgumentException.class, () -> user.setAge(200));
        assertFalse(user.equals("A"));
        assertEquals(7, new UserId(7).getId());
        assertEquals("UserId{id=7}", new UserId(7).toString());
        assertFalse(new UserId(7).equals("7"));

        assertTrue(ArraysDemo.deepEquals(new Object[][]{{"a"}}, new Object[][]{{"a"}}));
        assertEquals("[[a]]", ArraysDemo.deepToString(new Object[][]{{"a"}}));
        assertTrue(BigDecimalComparisonDemo.isLessOrEqual(BigDecimal.ONE, BigDecimal.ONE));
        assertTrue(BigDecimalComparisonDemo.isGreaterOrEqual(BigDecimal.TEN, BigDecimal.ONE));
        assertFalse(InstantZonedDateTimeDemo.isDaylightSavings(ZoneId.of("Asia/Shanghai"), Instant.EPOCH));
        assertDoesNotThrow(LocalDateDemo::today);
        assertEquals("x", ObjectsDemo.requireNonNullWithMessage("x", "message"));
        assertThrows(NullPointerException.class, () -> ObjectsDemo.requireNonNullWithMessage(null, "x"));
        assertDoesNotThrow(() -> RandomDemo.secureRandomWithAlgorithm("SHA1PRNG", 2));

        WebSpringDemo web = new WebSpringDemo();
        assertEquals("OK", web.statusMeaning(200));
        assertEquals("Bad Request", web.statusMeaning(400));
        assertEquals("Not Found", web.statusMeaning(404));
        assertEquals("Internal Server Error", web.statusMeaning(500));
        assertThrows(WebSpringDemo.UserNotFoundException.class, () -> new WebSpringDemo.UserService().find(2));
    }

    private DataSource h2() {
        JdbcDataSource h2 = new JdbcDataSource();
        h2.setURL("jdbc:h2:mem:" + System.nanoTime() + ";DB_CLOSE_DELAY=-1");
        h2.setUser("sa");
        return h2;
    }
}
