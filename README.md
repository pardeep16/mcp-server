# train-booking-mcp-server

Demo MCP server from The CodeReveal video **"MCP explained in 10 minutes (and build one in Java)"**.
Built with Spring Boot 4.1 + Spring AI 2.0.2 (`spring-ai-starter-mcp-server`, STDIO transport).

> The train data is **sample data** for learning. It is not connected to any real booking system.

## Tools
| Tool | What it does |
|---|---|
| `search_trains(from, to)` | Trains between two station codes (e.g. `NDLS` → `BCT`) |
| `check_seats(trainNo, travelClass)` | Seats left for a train and class (e.g. `12777`, `3A`) |

## 1. Requirements
- **Java 17 or newer** (check with `java -version`)
- Maven is optional: the included Maven wrapper (`mvnw`) downloads Maven 3.9.9 the first time.
- Node.js (only for the MCP Inspector step)

## 2. Build and test
```bash
# macOS / Linux
./mvnw clean package

# Windows
mvnw.cmd clean package
```
This runs the unit tests and creates `target/train-booking-mcp-server-1.0.0.jar`.

> Running the jar directly in a terminal looks like it "hangs": it is waiting for JSON-RPC messages on stdin. That is normal. Stop it with Ctrl+C.

## 3. Test with MCP Inspector
```bash
npx @modelcontextprotocol/inspector java -jar target/train-booking-mcp-server-1.0.0.jar
```
Open the URL it prints → **Connect** → **Tools** → **List Tools** → pick `check_seats`,
enter `12777` and `3A` → **Run Tool**. Expected result: `12 seats left in 3A on Sunrise Express`.

## 4. Connect it to Claude Desktop
Claude Desktop → Settings → Developer → **Edit Config**, then add (use your own absolute path):
```json
{
  "mcpServers": {
    "trains": {
      "command": "java",
      "args": ["-jar", "/ABSOLUTE/PATH/train-booking-mcp-server/target/train-booking-mcp-server-1.0.0.jar"]
    }
  }
}
```
Save, **quit and reopen** Claude Desktop, then ask: *"Are there any seats left in 3A on train 12777?"*

Tip: if the server doesn't start, Claude Desktop may not see `java` on your PATH. Use the full path instead:
- macOS: run `/usr/libexec/java_home` and use `<that path>/bin/java` as `"command"`
- Windows: e.g. `"C:\\Program Files\\Java\\jdk-21\\bin\\java.exe"`

## Why the properties matter
With STDIO, **stdout carries the MCP JSON-RPC messages**, so the startup banner is turned off and
`logback-spring.xml` sends all logs to **standard error** instead. Anything else printed to stdout would
corrupt the connection. (Claude Desktop shows a server's stderr in its MCP logs, handy for debugging.)

## Project layout
```
src/main/java/com/thecodereveal/trainbooking/
  TrainBookingMcpApplication.java   Spring Boot entry point
  Train.java                        sample data record
  TrainTools.java                   the two @McpTool methods
src/main/resources/application.properties
src/main/resources/logback-spring.xml      logs → stderr
src/test/java/.../TrainToolsTest.java
```
