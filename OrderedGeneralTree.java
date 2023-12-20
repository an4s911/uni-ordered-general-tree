import java.util.ArrayList;
import java.util.List;

/**
 * OrderedGeneralTree
 */
public class OrderedGeneralTree<E> extends AbstractTree<E> {

    protected static class Node<E> implements Position<E> {
        private E element; // an element stored at this node
        private Node<E> parent; // a reference to the parent node (if any)
        private Node<E> firstChild; // a reference to the left child (if any)
        private Node<E> nextSibling; // a reference to the right child (if any)

        /**
         * Constructs a node with the given element and neighbors.
         *
         * @param e           the element to be stored
         * @param above       reference to a parent node
         * @param firstChild  reference to a left child node
         * @param nextSibling reference to a right child node
         */
        public Node(E e, Node<E> above, Node<E> firstChild, Node<E> nextSibling) {
            this.element = e;
            this.parent = above;
            this.firstChild = firstChild;
            this.nextSibling = nextSibling;
        }

        // accessor methods
        public E getElement() {
            return element;
        }

        public Node<E> getParent() {
            return parent;
        }

        public Node<E> getFirstChild() {
            return firstChild;
        }

        public Node<E> getNextSibling() {
            return nextSibling;
        }

        // update methods
        public void setElement(E e) {
            element = e;
        }

        public void setParent(Node<E> parentNode) {
            parent = parentNode;
        }

        public void setFirstChild(Node<E> firstChild) {
            this.firstChild = firstChild;
        }

        public void setNextSibling(Node<E> nextSibling) {
            this.nextSibling = nextSibling;
        }
    } // ----------- end of nested Node class -----------

    /** Factory function to create a new node storing element e. */
    protected Node<E> createNode(E e, Node<E> parent, Node<E> first, Node<E> next) {
        return new Node<E>(e, parent, first, next);
    }

    // OrderedGeneralTree instance variables
    /** The root of the binary tree */
    protected Node<E> root = null; // root of the tree

    /** The number of nodes in the binary tree */
    private int size = 0; // number of nodes in the tree

    public OrderedGeneralTree() {
    }

    // nonpublic utility
    /**
     * Verifies that a Position belongs to the appropriate class, and is
     * not one that has been previously removed. Note that our current
     * implementation does not actually verify that the position belongs
     * to this particular list instance.
     *
     * @param p a Position (that should belong to this tree)
     * @return the underlying Node instance for the position
     * @throws IllegalArgumentException if an invalid position is detected
     */
    protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if (!(p instanceof Node)) {
            throw new IllegalArgumentException("Not valid position type");
        }
        Node<E> node = (Node<E>) p; // safe cast
        if (node.getParent() == node) // our convention for defunct node
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }

    // accessor methods (not already implemented in AbstractBinaryTree)
    /**
     * Returns the number of nodes in the tree.
     * 
     * @return number of nodes in the tree
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the root Position of the tree (or null if tree is empty).
     * 
     * @return root Position of the tree (or null if tree is empty)
     */
    @Override
    public Position<E> root() {
        return root;
    }

    /**
     * Returns the Position of p's parent (or null if p is root).
     *
     * @param p A valid Position within the tree
     * @return Position of p's parent (or null if p is root)
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    @Override
    public Position<E> parent(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return node.getParent();
    }

    /**
     * Returns the Position of p's left child (or null if no child exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the left child (or null if no child exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    public Position<E> firstChild(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return node.getFirstChild();
    }

    public Position<E> nextSibling(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return node.getNextSibling();
    }

    @Override
    public Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException {
        Position<E> curr = p;
        List<Position<E>> children = new ArrayList<>();
        while (curr != null) {
            children.add(curr);
            curr = nextSibling(curr);
        }
        return children;
    }

    /**
     * Returns the last child position of a given position in the tree.
     * (Helper method)
     *
     * @param p the position in the tree
     * @return the last child position of the given position, or null if there are
     *         no child positions
     */
    private Position<E> getLastChild(Position<E> p) {
        Position<E> walk = firstChild(p);

        if (walk == null) {
            return null;
        }

        while (nextSibling(walk) != null) {
            walk = nextSibling(walk);
        }

        return walk;
    }

    // update methods supported by this class
    /**
     * Places element e at the root of an empty tree and returns its new Position or
     * null if tree is not emtpy
     *
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalStateException if the tree is not empty
     */
    public Position<E> addRoot(E e) throws IllegalStateException {
        if (!isEmpty())
            return null;
        root = createNode(e, null, null, null);
        size = 1;
        return root;
    }

    /**
     * Adds a new child node with element e to the parent node p.
     *
     * @param p the parent node to which the new child node will be added
     * @param e the element to be stored in the new child node
     * @return the newly created child node
     */
    public Position<E> addChild(Position<E> p, E e) {
        Node<E> pNode = validate(p);
        Node<E> newNode = createNode(e, pNode, null, null);

        // if p has no children then set p's firstChild to the new node
        if (pNode.firstChild == null) {
            pNode.setFirstChild(newNode);
        } else {
            // otherwise set p's last child's nextSibling to the new node
            Node<E> lastChildOfP = validate(getLastChild(p));
            lastChildOfP.setNextSibling(newNode);
        }

        size++;
        return newNode;
    }

    /**
     * Removes the element at the given position and returns the element. If the
     * position is the root, returns null. Otherwise, removes the position from its
     * parent and updates the parent's child pointers accordingly.
     *
     * @param p The position to be removed.
     * @return The element of the position that was removed.
     */
    public E remove(Position<E> p) {

        if (p == root()) {
            return null;
        }

        Position<E> parent = parent(p);
        Node<E> parentNode = validate(parent);

        // Get next sibling of p
        Position<E> nextSiblingOfP = nextSibling(p);
        Node<E> nextSiblingOfPNode = null;
        if (nextSiblingOfP != null) {
            nextSiblingOfPNode = validate(nextSibling(p));
        }

        // walk the children of the parent
        Position<E> walk = firstChild(parent);

        // If p is the first child
        if (walk == p) {

            // If p doesn't have children
            if (firstChild(p) == null) {
                // Set p's parent's firstChild to the nextSibling of p
                parentNode.setFirstChild(nextSiblingOfPNode);
            } else {
                // Set p's last child's nextSibling to the nextSibling of p
                Node<E> lastChildOfP = validate(getLastChild(p));
                lastChildOfP.setNextSibling(nextSiblingOfPNode);

                // and set the firstChild of the parent to the firstChild of p
                Node<E> firstChildOfP = validate(firstChild(p));
                parentNode.setFirstChild(firstChildOfP);
            }
        } else {
            // Find the sibling that is before p
            while (nextSibling(walk) != p) {
                walk = nextSibling(walk);
            }
            Node<E> walkNode = validate(walk);

            // Set the previous sibling's nextSibling to the firstChild of p
            Position<E> firstChildOfP = firstChild(p);
            if (firstChildOfP != null) {
                Node<E> firstChildOfPNode = validate(firstChildOfP);
                walkNode.setNextSibling(firstChildOfPNode);

                // Set the nextSibling of the last child of p to the nextSibling of p
                Node<E> lastChildOfPNode = validate(getLastChild(p));
                lastChildOfPNode.setNextSibling(nextSiblingOfPNode);

                // Set the parent of the children of p to be parent of p
                while (firstChildOfP != null) {
                    firstChildOfPNode = validate(firstChildOfP);
                    firstChildOfPNode.setParent(parentNode);
                    firstChildOfP = nextSibling(firstChildOfPNode);
                }

            } else {
                walkNode.setNextSibling(nextSiblingOfPNode);
            }

        }

        // Store the element before resetting the position
        E element = p.getElement();

        size--;

        // Set position pointers to null
        Node<E> node = validate(p);
        node.setElement(null);
        node.setFirstChild(null);
        node.setNextSibling(null);
        node.setParent(node);

        return element;
    }

    // display methods

    /**
     * Display the tree.
     */
    public void displayTree() {
        displayTree(root(), 0);
    }

    /**
     * Displays the tree starting from the given position with a specified depth.
     *
     * @param p     the position to start the tree display from
     * @param depth the depth of the position in the tree
     */
    public void displayTree(Position<E> p, int depth) {
        if (p == null) {
            return;
        }

        System.out.println(". ".repeat(depth) + p.getElement());
        displayTree(firstChild(p), depth + 1);
        displayTree(nextSibling(p), depth);
    }

}