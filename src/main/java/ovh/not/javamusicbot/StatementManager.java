package ovh.not.javamusicbot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class StatementManager {
    final Map<Statement, String> statements = new HashMap<>();

    StatementManager() {
        for (Statement statement : Statement.values()) {
            try {
                statements.put(statement, load(statement));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private String load(Statement statement) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(statement.getFile()))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append(System.lineSeparator());
            }
            return builder.toString();
        }
    }
}
