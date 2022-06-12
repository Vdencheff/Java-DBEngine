package QBTask.QBDBManager;

import QBTask.Qbrow.*;
import java.util.*;  

public class QBDBManager 
{
    public QBDBManager()
    {
        colNum = 4;
        mDb = new Vector<Qbrow>();
        mLUT0= new HashMap<String, Vector<Integer>>();
        mLUT1= new HashMap<String, Vector<Integer>>();
        mLUT2= new HashMap<String, Vector<Integer>>();
        mLUT3= new HashMap<String, Vector<Integer>>();
        col = new String[colNum];
    }

    public QBDBManager(Vector<Qbrow> qbt)
    {
        this(); // calls default constructor
        mDb = qbt;
    }

    // print the whole database table
    public void printTable()
    {
        for (Qbrow it : mDb)
        {
            System.out.println( "c0= " + it.column0 );
            System.out.println( "c1= " + it.column1 );
            System.out.println( "c2= " + it.column2 );
            System.out.println( "c3= " + it.column3 );
        }
    }

    // print only one row of the database table
	// input params:
	//    row: the row number in the database table to be printed
    public void printTableRow(int row)
    {
        if (mDb.size() >= row)
        {
            System.out.println( "c0= " + mDb.get(row).column0 );
            System.out.println( "c1= " + mDb.get(row).column1 );
            System.out.println( "c2= " + mDb.get(row).column2 );
            System.out.println( "c3= " + mDb.get(row).column3 );
        }
    }

    // print look up table
	// input params:
	//    lut: look up table to be printed
    public void printLUT(HashMap<String, Vector<Integer>>  lut)
    {
        for (String iterKey : lut.keySet())
        {
            for(Integer iterValue :lut.get(iterKey))
            {
              System.out.println( iterKey + "==>" + iterValue);
            }
        }

    }
   
    // Update a look up table with new info or update the current info
	// input params:
	//    lut: look up table to be updated
	//    newKey: the key to be inserted or updated
	//    newValue: value to be inserted
    public void setElemInLUT(HashMap<String, Vector<Integer>> lut, String newKey, Integer newValue)
    {
        if(lut.get(newKey) != null) // such element exists, add the supplied value
        {
            if(lut.get(newKey).get(newValue) == null) // such vector value does not exist, add it
            {
                lut.get(newKey).addElement(newValue); // add the elem
            }
        }
        else // such element does not exist insert the supplied key and value
        {
            Vector<Integer> vecVal =  new Vector<>();
            vecVal.add(newValue);
            lut.put(newKey, vecVal);
        }

    }
    
    // fill the whole database table and related look up tables
	// input params:
	//    prefix: string to be used for data generation
	//    numRecords: how many new rows to be inserted into database
    public void populateQBTable(String prefix, int numRecords)
    {
        // update database
        for (int i = 0; i < numRecords; ++i)
        {
            Qbrow rec = new Qbrow(i, prefix + i, i % 100, i + prefix );
            mDb.addElement(rec);
        }
        // update look up tables
        populateLUT();
    }
    
    // Update all look up tables with the data from the database
    public void populateLUT()
    {
        for(int i  = 0; i < mDb.size(); i++)
        {
            setElemInLUT(mLUT0, mDb.get(i).column0.toString(), i);
            setElemInLUT(mLUT1, mDb.get(i).column1, i);
            setElemInLUT(mLUT2, mDb.get(i).column2.toString(), i);
            setElemInLUT(mLUT3, mDb.get(i).column3, i);
        }
    }
    
    // performing search in the database
	// input params:
	//    searchColumn: database column to search
	//    searchString: what to search for in the column
	// return: vector containing all database rows that matches search criteria
    public Vector<Qbrow> QBFindMatchingRecords(String searchColumn, String searchString)
    {
        Vector<Qbrow> res = new Vector<>();
        // find the matched items database position
        Vector<Integer> tempRes = PerformStrSearch(searchColumn, searchString);
        // take the matched items positions and extract all the whole rows from the database into new vector
        for (int it : tempRes)
        {
            res.addElement(mDb.get(it));
        }
        return res;
    }
    
    // Perform search in the database
	// input params:
	//    searchColumn: database column to search
	//    searchString: what to search for in the column
	// return: vector containing the positions of finded records in the database
    public Vector<Integer> PerformStrSearch(String searchColumn, String searchString)
    {
        if(searchColumn.equals("column0"))
        {
            if(null != mLUT0.get(searchString))
            {
                return mLUT0.get(searchString);
            }
        }
        if(searchColumn.equals("column1"))
        {
            if(null != mLUT1.get(searchString))
            {
                return mLUT1.get(searchString);
            }
        }
        if(searchColumn.equals("column2"))
        {
            if(null != mLUT2.get(searchString))
            {
                return mLUT2.get(searchString);
            }
        }
        if(searchColumn.equals("column3"))
        {
            if(null != mLUT3.get(searchString))
            {
                return mLUT3.get(searchString);
            }
        }
        else
        {
            System.out.println("Wrong column name supplied!");
        }
    
        return new Vector<>(); // return empty vector if search does not succeed
    }
    
    // deleting a row from the database. Look up tables are updated as well
	// input params:
	//    searchString: the unique id of the row to be deleted. Uniqie id is kept in column0
    public void deleteElem(String searchString)
    {
        Vector<Integer> retVal;
        // Find all the positions where the search item resides in the database
        // the returned vector must contain only one entry, because searchString contains unique id
        retVal = PerformStrSearch( "column0", searchString);
        if (retVal.size() != 0)
        {
            System.out.println("Element proposed for deletion found!");

            // gather info needed in deletion process
            int pos =retVal.get(0);
            //int pos = *retVal.begin(); // element position in the database
            // Extract all the fields that the searched item contains.
            // Each field will be searched in the corresponding look up table,
            // where the position of this searched element will be removed.
            // If the position of this searched element is the only value in the vector, 
            // the whole element is removed from the look up table.
            col[0] = mDb.elementAt(pos).column0.toString(); // the string representation of the column0 value of the element to be deleted
            col[1] = mDb.elementAt(pos).column1;            // the string representation of the column0 value of the element to be deleted
            col[2] = mDb.elementAt(pos).column2.toString(); // the string representation of the column0 value of the element to be deleted
            col[3] = mDb.elementAt(pos).column3;            // the string representation of the column0 value of the element to be deleted

            // erase from the database
            mDb.removeElementAt(pos);
            System.out.println("Database element that is to be deleted:");
            for (int i = 0; i < colNum; ++i)
            {
                System.out.println("column[" + i + "] = " + col[i]);
            }

            // erase from LUTs
            for (int i = 0; i < colNum; ++i)
            {
                deleteElemInLUT(i, pos);
            }
        }
        else
        {
            System.out.println("Element proposed for deletion not found");
        }
    }
    
    // delete element from look up table
	// input params:
	//    lutNo: look up table number
	//    pos: element position in the database
    public void deleteElemInLUT(int lutNo, int pos)
    {
        // find in the corresponding look up table the corresponding database column field
        if(lutNo == 0)
        {
            if(null != mLUT0.get(col[lutNo]))
            {
                // erase from the vector only the value corresponding to the deleted element position in the database
                int i = mLUT0.get(col[lutNo]).indexOf(pos);
                if(-1 != i)
                {
                    System.out.println("Searched element found in LUT " + lutNo + " ! Erasing it.");
                    mLUT0.get(col[lutNo]).remove(i); // erase the element position from the vector
                    // if the serached string is uniqie, then the whole entry must be removed, because after the row deletion it will not exist in the database
                    if(mLUT0.get(col[lutNo]).size() == 0)
                    {
                        mLUT0.remove(col[lutNo]);
                    }
                }
            }
            else
            {
                System.out.println("Element proposed for deletion not found in LUT0!");
            }
        }
        if(lutNo == 1)
        {
            if(null != mLUT1.get(col[lutNo]))
            {
                // erase from the vector only the value corresponding to the deleted element position in the database
                int i = mLUT1.get(col[lutNo]).indexOf(pos);
                if(-1 != i)
                {
                    System.out.println("Searched element found in LUT " + lutNo + " ! Erasing it.");
                    mLUT1.get(col[lutNo]).remove(i); // erase the element position from the vector
                    // if the serached string is uniqie, then the whole entry must be removed, because after the row deletion it will not exist in the database
                    if(mLUT1.get(col[lutNo]).size() == 0)
                    {
                        mLUT1.remove(col[lutNo]);
                    }
                }
            }
            else
            {
                System.out.println("Element proposed for deletion not found in LUT1!");
            }
        }
        if(lutNo == 2)
        {
            if(null != mLUT2.get(col[lutNo]))
            {
                // erase from the vector only the value corresponding to the deleted element position in the database
                int i = mLUT2.get(col[lutNo]).indexOf(pos);
                if(-1 != i)
                {
                    System.out.println("Searched element found in LUT " + lutNo + " ! Erasing it.");
                    mLUT2.get(col[lutNo]).remove(i); // erase the element position from the vector
                    // if the serached string is uniqie, then the whole entry must be removed, because after the row deletion it will not exist in the database
                    if(mLUT2.get(col[lutNo]).size() == 0)
                    {
                        mLUT2.remove(col[lutNo]);
                    }
                }
            }
            else
            {
                System.out.println("Element proposed for deletion not found in LUT2!");
            }
        }
        if(lutNo == 3)
        {
            if(null != mLUT3.get(col[lutNo]))
            {
                // erase from the vector only the value corresponding to the deleted element position in the database
                int i = mLUT3.get(col[lutNo]).indexOf(pos);
                if(-1 != i)
                {
                    System.out.println("Searched element found in LUT " + lutNo + " ! Erasing it.");
                    mLUT3.get(col[lutNo]).remove(i); // erase the element position from the vector
                    // if the serached string is uniqie, then the whole entry must be removed, because after the row deletion it will not exist in the database
                    if(mLUT3.get(col[lutNo]).size() == 0)
                    {
                        mLUT3.remove(col[lutNo]);
                    }
                }
            }
            else
            {
                System.out.println("Element proposed for deletion not found in LUT1!");
            }
        }
    }
    
    private int colNum;
    private Vector<Qbrow> mDb;
    private HashMap<String, Vector<Integer>> mLUT0;
    private HashMap<String, Vector<Integer>> mLUT1;
    private HashMap<String, Vector<Integer>> mLUT2;
    private HashMap<String, Vector<Integer>> mLUT3;
    private String col[];
}
