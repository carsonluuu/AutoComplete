class AutocompleteSystem {

    class TrieNode {
        public boolean isWord;
        public TrieNode[] children;
        Map<String, Integer> counts;
        public TrieNode() {
            isWord = false;
            children = new TrieNode[27]; //add /space
            counts = new HashMap<String, Integer>();
        }
    }

    class Pair {
        String s;
        int count;
        public Pair(String s, int count) {
            this.s = s;
            this.count = count;
        }
    }

    private TrieNode root;
    private String prefix;

    public AutocompleteSystem(String[] sentences, int[] times) {
        root = new TrieNode();
        prefix = "";
        for (int i = 0; i < sentences.length; i++) {
            insert(sentences[i], times[i]);
        }
    }

    private int int_(char c) {
        return c == ' ' ? 26 : c - 'a';
    }

    private void insert(String word, int count) {
        TrieNode p = root;
        for (int i = 0; i < word.length(); i++) {
            int index = int_(word.charAt(i));
            if (p.children[index] == null) {
                p.children[index] = new TrieNode();
            }
            p = p.children[index];
            p.counts.put(word, p.counts.getOrDefault(word, 0) + count);
        }
        p.isWord = true;
    }

    private TrieNode find(String prefix) {
        TrieNode p = root;
        for (int i = 0; i < prefix.length(); i++) {
            int index = int_(prefix.charAt(i));
            if (p.children[index] == null) {
                return null;
            }
            p = p.children[index];
        }
        return p;
    }

    public List<String> input(char c) {
        List<String> res = new ArrayList<String>();
        if (c == '#') {
            insert(prefix, 1);
            prefix = "";
            return res;
        }
        prefix += c;
        TrieNode curr = find(prefix);
        if (curr == null) {
            return res;
        }

        PriorityQueue<Pair> pq = new PriorityQueue<>(
                (a, b) -> (a.count == b.count ? a.s.compareTo(b.s) : b.count - a.count)
        );

        for (String s : curr.counts.keySet()) {
            pq.add(new Pair(s, curr.counts.get(s)));
        }

        for (int i = 0; i < 3 && !pq.isEmpty(); i++) {
            res.add(pq.poll().s);
        }

        return res;
    }
}

/**
 * Your AutocompleteSystem object will be instantiated and called as such:
 * AutocompleteSystem obj = new AutocompleteSystem(sentences, times);
 * List<String> param_1 = obj.input(c);
 */
