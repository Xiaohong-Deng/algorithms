package LeetCode.hard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemorySystem {
    public static class Dir {
        private Map<String, InMemorySystem.Dir> dirs;
        private Map<String, String> files;

        public Dir() {
            dirs = new HashMap<>();
            files = new HashMap<>();
        }
    }

    Dir root;
    public InMemorySystem() {
        root = new Dir();
    }
    
    public List<String> ls(String path) {
        Dir t = root;
        List < String > files = new ArrayList < > ();
        if (!path.equals("/")) {
            String[] d = path.split("/");
            // recursively get to the bottom but not including bottom, because bottom might be a file name
            for (int i = 1; i < d.length - 1; i++) {
                t = t.dirs.get(d[i]);
            }
            // if it is a file, it is the only one to return
            if (t.files.containsKey(d[d.length - 1])) {
                files.add(d[d.length - 1]);
                return files;
            } else {
                t = t.dirs.get(d[d.length - 1]);
            }
        }
        files.addAll(new ArrayList < > (t.dirs.keySet()));
        files.addAll(new ArrayList < > (t.files.keySet()));
        Collections.sort(files);
        return files;
    }
    
    public void mkdir(String path) {
        Dir t = root;
        String[] d = path.split("/");
        // first is empty string, if path is root we do not do anything
        // otherwise we check every subsequent directory to see if it exists
        for (int i = 1; i < d.length; i++) {
            if (!t.dirs.containsKey(d[i]))
                t.dirs.put(d[i], new Dir());
            t = t.dirs.get(d[i]);
        }
    }
    
    public void addContentToFile(String filePath, String content) {
        Dir t = root;
        String[] d = filePath.split("/");
        for (int i = 1; i < d.length - 1; i++) {
            t = t.dirs.get(d[i]);
        }

        // file may exist or not
        if(t.files.containsKey(d[d.length - 1]))
            t.files.put(d[d.length - 1], t.files.get(d[d.length - 1]) + content);
        else
        t.files.put(d[d.length - 1], content);
    }
    
    public String readContentFromFile(String filePath) {
        Dir t = root;
        String[] d = filePath.split("/");
        for (int i = 1; i < d.length - 1; i++) {
            t = t.dirs.get(d[i]);
        }
        String content = t.files.get(d[d.length - 1]);
        return content;
    }
}
