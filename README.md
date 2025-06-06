# tp3partie2
## Analysis and Comparison

### Existing Tests
- Since this is a new project, there are no pre-existing tests.
- Hypothetically, tests using mocks or in-memory databases (e.g., H2) would lack realism and isolation compared to Testcontainers.

### Testcontainers Tests
- **Coverage**: Tests cover creating, retrieving, and deleting tasks, ensuring core functionality.
- **Readability**: Tests are clear, with descriptive method names and assertions.
- **Maintainability**: Testcontainers simplifies container management, reducing setup code.
- **Reliability**: Real MySQL containers ensure tests reflect production behavior.

### Advantages of Testcontainers
- **Isolation**: Each test runs in a fresh container, preventing state leakage.
- **Realism**: Uses a real MySQL database, mimicking production.
- **Automation**: Containers are started/stopped automatically.

### Disadvantages
- **Performance**: Tests are slower due to container startup time.
- **Dependency on Docker**: Requires Docker to be installed and running.

### Conclusion
Testcontainers significantly improves test reliability and realism compared to mocks or in-memory databases, at the cost of slightly slower execution.