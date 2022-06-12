

/*
searched item- element of the structure of the data base: column1, column1 etc.

To increase search procedure, the searched items must be processed.
Some examples for such processing:
1. Add hasesh to each search element
2. Sort search elements
3. Create tree out of search elements

The return result is a vector of data structures. Searched item is one element of this data structure
Due to that fact the above methods are more or less appropriate
1. Adding hashes will not work if they are added as additional fieled in the structure. Hashes must be sorted to be able to profit from them.
And we cannot sort by all elements of the table at the same time, unless we create separate structure for each element of the data structure.
Unordered map uses hashes and sorts them. This is a good choise for increase search speed, but it has increased memory consumption and create database time.
The unordered map must be external for the data base structure. There will be one unordered map for each search item.
The key part of the unordered map is the element of the data structure, the value part is vector of numbers showing at which position in the database vector
the searched element is found. Then by just accessing by index the results from the database vector are extracted.
This is the approach in the proposed solution.

2. Sorting each element is not appropriate, because multiple copies of the database must be present - one copy for each element of the structure

3. Create tree is a good approach. The tree will substitute the unordered map proposed above. The nodes are the keys.
Each node that corresponds to valid search item will have an vector element,
that points to where in the database vector the search item is found. The thee has advantages to be less memory greedy than unordered map,
because no hashes are needed, and not need to store each search item completely (imagine creating a dictionary).
The search time is expected to be fast, because the search time depends only on the number of letters of the search string,
and on how many places each reached node points to.


If large enough data base table is required, exception could be thrown. Check all memory reservation opertions.
*/
package QBTask.Entry;

import QBTask.QBDBManager.QBDBManager;
import QBTask.Qbrow.*;
import java.util.*; 
public class Entry 
{
    public static void printRes(Vector<Qbrow> searchResults)
    {
        System.out.println( "Search results:");
        for (Qbrow it : searchResults)
        {
            System.out.println("c0= " + it.column0);
            System.out.println("c1= " + it.column1);
            System.out.println("c2= " + it.column2);
            System.out.println("c3= " + it.column3);
        }
    }
    public static void main(String args[])
    {

        String poplulateStr = "testdata";
        // test 1
        System.out.println("!!!!!!!!!! TEST 1 !!!!!!!!!!");
        QBDBManager dbManager = new QBDBManager();
        dbManager.populateQBTable(poplulateStr, 4);
        Vector<Qbrow>  res =  new Vector<Qbrow>();
        String searchStr = "2";
        res = dbManager.QBFindMatchingRecords("column0", searchStr);
        printRes(res);
        assert(res.size() == 1);
        System.out.println("Print table before deletion!");
        dbManager.printTable();
        System.out.println("Deletion follows!");
        dbManager.deleteElem(searchStr);
        System.out.println("Print table after deletion!");
        dbManager.printTable();
        System.out.println("Search in column0 for " + searchStr);
        res = dbManager.QBFindMatchingRecords("column0", searchStr);
        printRes(res);
        assert(res.size() == 0);
    }
    
}
