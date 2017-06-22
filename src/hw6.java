import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Node {
    public int val;
    public Node left, right, parent;
    public boolean black;
    public Node(int new_val) {
        this.val = new_val;
        this.left = RBTree.nil;
        this.right = RBTree.nil;
        this.parent = RBTree.nil;
        black = false;
    }
    public Node() {
        this.val = 0;
        this.left = RBTree.nil;
        this.right = RBTree.nil;
        this.parent = RBTree.nil;
        black = true;
    }

    public boolean isLeaf() {
        return left == RBTree.nil && right == RBTree.nil;
    }
}

class RBTree {
    static public Node nil = new Node();
    public Node root = nil;
    RBTree() {
        root = nil;
    }

    void left_rotate(RBTree T, Node x) {
        if (x.right == nil)
            return;
        Node y = x.right;
        x.right = y.left;
        if(y.left != T.nil)
            y.left.parent = x;
        y.parent = x.parent;
        if(x.parent == T.nil)
            T.root = y;
        else if (x == x.parent.left)
            x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }
    void right_rotate(RBTree T, Node y) {
        if (y.left == nil)
            return;
        Node x = y.left;
        y.left = x.right;
        if(x.right != T.nil)
            x.right.parent = y;
        x.parent = y.parent;
        if(y.parent == T.nil)
            T.root = x;
        else if (y == y.parent.right)
            y.parent.right = x;
        else y.parent.left = x;
        x.right = y;
        y.parent = x;
    }

    public void insert(RBTree tree, Node n) {
        Node y = nil;
        Node x = root;
        while (x != nil) {
            y = x;
            if (n.val < x.val) {
                x = x.left;
            }
            else x = x.right;
        }
        n.parent = y;

        if(y == nil)
            tree.root = n;
        else if (n.val < y.val)
            y.left = n;
        else y.right = n;
        n.left = tree.nil;
        n.right = tree.nil;
        n.black = false;
        RB_Insert_Fixup(tree, n);
    }
    public void RB_Insert_Fixup(RBTree T, Node n) {
        if(n.parent == nil)
            return;
        if(n.parent.parent == nil)
            return;
        Node y;

        while (n.parent.black == false) {
            if (n.parent == n.parent.parent.left) {
                y = n.parent.parent.right;
                if(y.black == false) {
                    n.parent.black = true;
                    y.black = true;
                    n.parent.parent.black = false;
                    n = n.parent.parent;
                }
                else {
                    if (n == n.parent.right) {
                        n = n.parent;
                        left_rotate(T,n);
                    }

                    n.parent.black = true;
                    n.parent.parent.black = false;
                    right_rotate(T,n.parent.parent);
                }
            }
            else {
                y = n.parent.parent.left;
                if(y.black == false) {
                    n.parent.black = true;
                    y.black = true;
                    n.parent.parent.black = false;
                    n = n.parent.parent;
                }
                else {
                    if (n == n.parent.left){
                        n = n.parent;
                        right_rotate(T,n);
                    }
                    n.parent.black = true;
                    n.parent.parent.black = false;
                    left_rotate(T,n.parent.parent);
                }
            }
        }
        T.root.black = true;
    }

    public void delete(RBTree T, Node z) {
        Node x;
        Node y = z;
        boolean y_original_color = y.black;
        if(z.left == T.nil) {
            x = z.right;
            RB_Transplant(T, z, z.right);
        }
        else if (z.right == T.nil) {
            x = z.left;
            RB_Transplant(T, z, z.left);
        }
        else {
            y = Tree_Minimum(z.right);
            y_original_color = y.black;
            x = y.right;
            if (y.parent != z) {
                RB_Transplant(T,y,y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            else
                x.parent = y;
            RB_Transplant(T,z,y);
            y.left = z.left;
            y.left.parent = y;
            y.black = z.black;
        }
        if (y_original_color == true)
            RB_Delete_Fixup(T, x);
    }
    public void RB_Delete_Fixup(RBTree T, Node x) {Node w;
        while (x != root && x.black  == true) {
            if (x == x.parent.left) {
                w = x.parent.right;
                if (w.black == false) {
                    w.black = true;
                    x.parent.black = false;
                    left_rotate(T, x.parent);
                    w = x.parent.right;
                }
                if (w.left.black == true && w.right.black == true) {
                    w.black = false;
                    x = x.parent;
                } else {
                    if (w.right.black == true) {
                        w.left.black = true;
                        w.black = false;
                        right_rotate(T, w);
                        w = x.parent.right;
                    }
                    w.black = x.parent.black;
                    x.parent.black = true;
                    w.right.black = true;
                    left_rotate(T, x.parent);
                    x = root;
                }
            } else {
                w = x.parent.left;
                if (w.black == false) {
                    w.black = true;
                    x.parent.black = false;
                    right_rotate(T, x.parent);
                    w = x.parent.left;
                }
                if (w.right.black == true && w.left.black == true) {
                    w.black = false;
                    x = x.parent;
                } else {
                    if (w.left.black == true) {
                        w.right.black = true;
                        w.black = false;
                        left_rotate(T, w);
                        w = x.parent.left;
                    }
                    w.black = x.parent.black;
                    x.parent.black = true;
                    w.left.black = true;
                    right_rotate(T, x.parent);
                    x = root;
                }
            }
        }
        x.black  = true;
    }

    public void RB_Transplant(RBTree T, Node u, Node v){
        if (u.parent == T.nil)
            T.root = v;
        else if (u == u.parent.left)
            u.parent.left = v;
        else u.parent.right = v;
        v.parent = u.parent;
    }
    public Node Tree_Minimum(Node x) {
        while (x.left != nil) {
            x = x.left;
        }
        return x;
    }

    public Node Tree_Maximum(Node x) {
        while (x.right != nil) {
            x = x.right;
        }
        return x;
    }

    public Node search (Node tree, int val) {
        if (tree == nil)
            return nil;
        else if (val == tree.val)
            return tree;
        else if (val < tree.val) {
            if(tree.left == nil) {
                Node a = nil;
                a.left = tree.left;
                a.right = tree.right;
                return a;
            }
            return search(tree.left, val);
        }
        else {
            if(tree.right == nil) {
                Node a = nil;
                a.left = tree.left;
                a.right = tree.right;
                return a;
            }
            return search(tree.right, val);
        }
    }
    void print(Node tree) {
        if (tree.left != nil)
            print(tree.left);
        System.out.print(tree.val);
        if (tree.black == false)
            System.out.println(" R");
        else
            System.out.println(" B");

        if (tree.right != nil)
            print(tree.right);
    }
    void inorder(Node tree) {
        if (tree == nil)
            return;
        else {
            inorder(tree.left);
            System.out.print(" " + tree.val);
            inorder(tree.right);
        }
    }
    public void inorder_iter() {
        if (root == nil)
            return;
        Stack stack = new Stack();
        Node node = root;
        while (node != nil) {
            stack.push(node);
            node = node.left;
        }
        while (!stack.is_empty()) {
            node = stack.pop();
            System.out.print(node.val + " ");
            if (node.right != nil) {
                node = node.right;
                while (node != nil) {
                    stack.push(node);
                    node = node.left;
                }
            }
        }
    }

    int GetBlackNode() {
        return GetBlackNode(root);
    }
    int GetBlackNode(Node root) {
        if(root == nil) {
            return 0;
        }
        else {
            int tmp;
            tmp = !root.black || root.isLeaf() ? 0 : 1;
            return GetBlackNode(root.left) + GetBlackNode(root.right) + tmp;
        }
    }
    int GetBlackHeight() {
        Node x = root;
        int count = 0;
        while (x != nil) {
            if(x.black) {
                count++;
            }
            x = x.left;
        }
        return count;
    }
}

class Stack {
    ArrayList stk = new ArrayList<Node>();
    int top = 0;
    void push(Node a) {
        stk.add(a);
    }
    Node pop() {
        return (Node)stk.remove(stk.size() - 1);
    }
    boolean is_empty() {
        return top == 0;
    }
}





public class hw6 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        RBTree rb = new RBTree();
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            line = line.trim();
            int num = Integer.parseInt(line);
            if (num > 0) {
                rb.insert(rb, new Node(num));
            } else if (num < 0) {
                if (rb.search(rb.root, -num) != rb.nil) {
                    rb.delete(rb, rb.search(rb.root, -num));
                }
            } else
                break;
        }
        br = new BufferedReader(new FileReader("search.txt"));
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            line = line.trim();
            int num = Integer.parseInt(line);
            if (num == 0) break;
            Node a = rb.search(rb.root, num);
            Node b = rb.Tree_Minimum(a.right);
            Node c = rb.Tree_Maximum(a.left);
            if(c.val == 0) {
                System.out.print("NIL ");
            }
            else
                System.out.print(c.val + " ");
            if(a.val == 0) {
                System.out.print("NIL ");
            }
            else
                System.out.print(a.val + " ");
            if(b.val == 0) {
                System.out.println("NIL");
            }
            else
                System.out.println(b.val);

        }
        br.close();
    }
}

