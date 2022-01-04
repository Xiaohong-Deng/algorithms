package LeetCode.medium;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class kSum {
    public int[] twoSum(int[] nums, int target) {
        int[] ans = new int[2];
        HashMap<Integer, Integer> num_to_idx = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int comp = target - nums[i];
            if (num_to_idx.containsKey(comp)) {
                ans[0] = num_to_idx.get(comp);
                ans[1] = i;
                return ans;
            }
            num_to_idx.put(nums[i], i);
        }

        return ans;
    }

    public List<List<Integer>> threeSum(int[] nums, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            // skip the same i in i + j + k
            if (i > 0 && nums[i - 1] == nums[i]) {
                continue;
            }

            int comp = target - nums[i];

            int left, right;
            left = i + 1;
            right = nums.length - 1;

            while (left < right) {
                if (nums[left] + nums[right] < comp) {
                    left++;
                    while (nums[left - 1] == nums[left]) {
                        left++;
                    }
                } else if (nums[left] + nums[right] > comp) {
                    right--;
                    while (nums[right] == nums[right + 1]) {
                        right--;
                    }
                } else {
                    ans.add(Arrays.asList(i, left, right));
                }
            }
        }
        return ans;
    }

    public int getKSum(int[] nums, int target, int k) {
        
    }
}
