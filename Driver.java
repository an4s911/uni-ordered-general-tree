public class Driver {
    public static void main(String[] args) {

        OrderedGeneralTree<String> tree = new OrderedGeneralTree<>();

        Position<String> A = tree.addRoot("A");
        Position<String> B = tree.addChild(A, "B");
        tree.addChild(A, "C");
        Position<String> D = tree.addChild(A, "D");

        tree.addChild(B, "E");
        tree.addChild(B, "F");

        Position<String> G = tree.addChild(D, "G");
        tree.addChild(D, "Q");

        System.out.println("-".repeat(20) + " DISPLAY TREE " + "-".repeat(20));
        System.out.println("Size: " + tree.size());
        tree.displayTree();

        System.out.println("-".repeat(20) + " REMOVE B " + "-".repeat(20));
        tree.remove(B);
        System.out.println("Size: " + tree.size());
        tree.displayTree();

        System.out.println("-".repeat(20) + " REMOVE G " + "-".repeat(20));
        tree.remove(G);
        System.out.println("Size: " + tree.size());
        tree.displayTree();

        System.out.println("-".repeat(20) + " REMOVE D " + "-".repeat(20));
        tree.remove(D);
        System.out.println("Size: " + tree.size());
        tree.displayTree();

        System.out.println("-".repeat(20) + " REMOVE A " + "-".repeat(20));
        tree.remove(A);
        System.out.println("Size: " + tree.size());
        tree.displayTree();

    }
}
