# mocksocket

Allows using sockets without having to grant dangerous permissions to all of your code.

This wraps the Socket and ServerSocket apis with AccessController blocks. No code changes are needed.

Instead of:

    grant {
      // give scary permission to all code just for tests
      permission java.net.SocketPermission "*", "accept,connect,resolve";
    };

You can do:

    <dependency>
      <groupId>org.elasticsearch</groupId>
      <artifactId>mocksocket</artifactId>
      <version>1.0</version>
      <scope>test</scope>
    </dependency>
    ...
    grant codeBase "/url/to/mocksocket-1.0.jar" {
      // only allow this jar used in tests to do this
      permission java.net.SocketPermission "*", "accept,connect,resolve";
    };