package net.chesstango.search.gamegraph;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GameMockLoader {

    public static Node loadFromFile() {
        try {
            InputStream inputStream = GameMock.class.getClassLoader().getResourceAsStream("GameGraph.json");

            // reading the files with buffered reader
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(inputStreamReader, Node.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Node mockNode = loadFromFile();

        mockNode.accept(new NodeFixParentLink());

        System.out.println(mockNode);
    }

    private static class NodeFixParentLink implements NodeVisitor{
        private Node parentNode;

        @Override
        public void visit(Node node) {
            node.parentNode = parentNode;
        }

        @Override
        public void visit(NodeLink nodeLink) {
            this.parentNode = nodeLink.parent;
        }
    }

}
