/*
 *
 *  * Copyright (c) 2019 Alexander Grün
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */

package de.unknownreality.dataframe.join.impl;

import de.unknownreality.dataframe.DataFrame;
import de.unknownreality.dataframe.DataRow;
import de.unknownreality.dataframe.join.JoinColumn;
import de.unknownreality.dataframe.join.JoinInfo;
import de.unknownreality.dataframe.join.JoinedDataFrame;

import static de.unknownreality.dataframe.join.impl.JoinOperationUtil.*;

/**
 * Created by Alex on 10.07.2016.
 */
public class DirectionJoinUtil{
    /**
     * Creates a direction (left or right) join
     *
     * @param dfA         first data frame
     * @param dfB         second data frame
     * @param joinInfo    info about the columns in the joined data frame
     * @param joinColumns columns used for the join
     * @return joined data frame
     */
    public static JoinedDataFrame createDirectionJoin(DataFrame dfA, DataFrame dfB,
                                               JoinInfo joinInfo, JoinColumn[] joinColumns) {

        int[] joinIndicesA = JoinOperationUtil.getJoinIndices(dfA,joinInfo);
        int[] joinIndicesB = JoinOperationUtil.getJoinIndices(dfB,joinInfo);
        int joinSize = joinInfo.getHeader().size();
        JoinedDataFrame joinedDataFrame = new JoinedDataFrame(joinInfo);
        joinedDataFrame.set(joinInfo.getHeader());
        JoinTree joinTree = new JoinTree(JoinTree.LeafMode.FirstOnly,dfA,dfB, joinColumns);
        for(JoinTree.JoinNode node : joinTree.getSavedLeafs()){
            for(Integer rowA : node.getIndicesA()){
                DataRow dataRowA = dfA.getRow(rowA);
                if(node.getIndicesB() == null){
                    Object[] joinedRowValues = new Object[joinSize];
                    fillValues(joinIndicesA, dataRowA, joinedRowValues);
                    fillNA(joinedRowValues);
                    joinedDataFrame.append(joinedRowValues);
                    continue;
                }
                appendGroupJoinedRows(node.getIndicesB(),dfB,dataRowA,joinIndicesA,joinIndicesB,joinSize,joinedDataFrame);

            }
        }
        return joinedDataFrame;
    }
}
