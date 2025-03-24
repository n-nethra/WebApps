import java.util.*;
public class Trie{
    private TrieNode root;
//may consider using an int to count total words or nodes
//consider a reference to maintain current node

    public Trie(){
        root = new TrieNode();
    }

    public void insert(String word){
        TrieNode curr = root;


        for (char c : word.toCharArray()){
            Map<Character, TrieNode> kids = curr.getChildren();

            if (kids.containsKey(c)){
                curr = kids.get(c);
                curr.incrementCount();
            }

            else {
                kids.put(c, new TrieNode());
                curr = kids.get(c);
            }

        }

        curr.setEndOfWord();

    }

    public boolean contains(String word){
        if (word.isEmpty())
            return false;

        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            Map<Character, TrieNode> kids = curr.getChildren();
            if (kids.containsKey(c)) {
                curr = kids.get(c);
            } else {
                return false;
            }
        }
        return curr.isEndOfWord();  //ensures it's a full word and not a prefix
    }


    public char mostLikelyNextChar(String str) {
        TrieNode curr = goToNode(str);
        if (curr == null) {
            return '_'; // No valid path exists
        }

        Map<Character, TrieNode> kids = curr.getChildren();
        if (kids.isEmpty()) {
            return '_'; // No possible next characters
        }

        return kids.entrySet().stream().max(Comparator.comparingInt(entry -> entry.getValue().getCount())).get().getKey();
    }

    private TrieNode goToNode(String str){
        TrieNode curr = root;

        for (char c : str.toCharArray()){
            Map<Character, TrieNode> kids = curr.getChildren();

            if (!kids.containsKey(c)){
                return null;
            }

            curr = kids.get(c);
        }

        return curr;
    }

    public Map<Character, Integer> likelyNextCharFrequency(String str) {
        TrieNode curr = goToNode(str);
        if (curr == null) {
            return new HashMap<>();
        }

        Map<Character, TrieNode> kids = curr.getChildren();
        List<Map.Entry<Character, TrieNode>> sortedList = new ArrayList<>(kids.entrySet());

        // Sort in descending order of frequency
        sortedList.sort((a, b) -> Integer.compare(b.getValue().getCount(), a.getValue().getCount()));

        // Store top 5
        Map<Character, Integer> top5 = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(5, sortedList.size()); i++) {
            top5.put(sortedList.get(i).getKey(), sortedList.get(i).getValue().getCount());
        }

        return top5;
    }

    public TreeMap<String, Integer> getWords(String str) {
        TrieNode current = goToNode(str);
        if (current == null) {
            return new TreeMap<>();
        }

        TreeMap<String, Integer> words = new TreeMap<>();
        collectWords(current, new StringBuilder(str), words);

        // Sort in descending order of frequency
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(words.entrySet());
        sortedList.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        // Get top 5
        TreeMap<String, Integer> top5 = new TreeMap<>();
        for (int i = 0; i < Math.min(5, sortedList.size()); i++) {
            top5.put(sortedList.get(i).getKey(), sortedList.get(i).getValue());
        }

        return top5;
    }


    //helper method to recursively collect words from a given TrieNode
    public void collectWords(TrieNode current, StringBuilder prefix, TreeMap<String, Integer> words){
    //if end of word, add details to data structure

        if(current.isEndOfWord()){
            words.put(prefix.toString(), current.getCount());
        }

        for(Map.Entry<Character, TrieNode> entry : current.getChildren().entrySet()){
        //add letter from map to prefix
            prefix.append(entry.getKey());
        //recurse onwards
            collectWords(entry.getValue(), prefix, words);
        //backtrack
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }


    class TrieNode{
        int count; //count # of times path is taken
        int wordEndCount; //# of times the full word appears
        Map<Character, TrieNode> children; //# of times the full word appears

        public TrieNode(){
            count = 1;
            wordEndCount = 0;
            children = new HashMap<Character, TrieNode>();
        }

        private Map<Character, TrieNode> getChildren(){
            return children;
        }

        private void incrementCount(){
            count++;
        }

        private int getCount(){
            return count;
        }

        private boolean isEndOfWord(){
            return (wordEndCount > 0);
        }

        private void setEndOfWord(){
            this.wordEndCount++;
        }

        public String toString(){
            return "(" + count + ", " + wordEndCount + ")";

        }

    }

}