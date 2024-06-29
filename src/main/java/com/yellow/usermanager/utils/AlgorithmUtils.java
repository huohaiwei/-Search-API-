package com.yellow.usermanager.utils;

import java.util.List;
import java.util.Objects;

/**
 * 算法工具类
 * @author 陈翰垒
 */
public class AlgorithmUtils {

    /**
     * 编辑距离算法（用于计算相似的两组标签）
     * @param tagListA 标签组A
     * @param tagListB 标签组B
     * @return 从A->B所要的最小步数，也就是相似度
     */
    public static int minDistance(List<String> tagListA, List<String> tagListB){
        int n = tagListA.size();
        int m = tagListB.size();

        if(n * m == 0) {
            return n + m;
        }
        int[][] d = new int[n + 1][m + 1];
        for (int i = 0; i < n + 1; i++){
            d[i][0] = i;
        }

        for (int j = 0; j < m + 1; j++){
            d[0][j] = j;
        }

        for (int i = 1; i < n + 1; i++){
            for (int j = 1; j < m + 1; j++){
                int left = d[i - 1][j] + 1;
                int down = d[i][j - 1] + 1;
                int left_down = d[i - 1][j - 1];
                if (!Objects.equals(tagListA.get(i - 1), tagListB.get(j - 1))) {
                    left_down += 1;
                }
                d[i][j] = Math.min(left, Math.min(down, left_down));
            }
        }
        return d[n][m];
    }
}
