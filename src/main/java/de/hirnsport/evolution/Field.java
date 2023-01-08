package de.hirnsport.evolution;

import java.util.HashSet;
import java.util.Set;

public class Field {

	public final static int MAX_WIDTH = 8;
	public final static int MAX_HEIGHT = 8;
	
    public final static char EMPTY_CELL = '_' ;
    public int multiUsedChars = 0;
    public int wordCount = 0;
    public int violations = 0;
    public int letters = 0;
    
    public enum DirectionEnum {horizontal, vertical, diagonal}

	private final char[][] field;
    
    private Set<String> usedWords = new HashSet<String>();
    
    public Field( )
    {
    	field = new char[MAX_HEIGHT][MAX_WIDTH];
		reset();
    }
    
    public void reset()
    {
    	for (int y =0; y<MAX_HEIGHT; y++)
    	{
    		for (int x =0; x<MAX_WIDTH; x++)
        	{
    			field[y][x] = EMPTY_CELL;
        	}
    	}
    	usedWords.clear();
    	multiUsedChars = 0;
    	wordCount = 0;
    }
    
    public String printToString()
    {
    	String matrix = "";
    	for (int y =0; y<MAX_HEIGHT; y++)
    	{
    		String line = "";
    		for (int x =0; x<MAX_WIDTH; x++)
        	{
    			line = line + field[y][x];
        	}
    		matrix = matrix + line +"\n";
    	}
    	return matrix;
    }
    
    private boolean putCharInField(int x, int y, char c)
    {
    	if ( ( x < 0 ) || ( x >= MAX_WIDTH ) ||  ( y < 0 ) || ( y >= MAX_HEIGHT ) )
    	{
    		return false;
    	}
    	if ( field[y][x] == c )
    	{
    		multiUsedChars++;
    		return true;
    	}
    	if (field[y][x] == EMPTY_CELL)
    	{
    		field[y][x] = c;
    		return true;
    	} else
    	{
    		return false;
    	}
    }
    
    public int putWord( String word, int x, int y, DirectionEnum direction )
    {
    	int lenOfWord = word.length();
    	int _violations  = 0;
    	int xOffset = 0;
    	int yOffset = 0;
    	
    	if (usedWords.contains(word))
    	{
    		return 0;
    	}
    	this.wordCount++;
    	usedWords.add(word);
    	
    	if ( direction == DirectionEnum.horizontal )
    	{
    		xOffset = 1;
    	} else if ( direction == DirectionEnum.vertical)
    	{
    		yOffset = 1;
    	} else if ( direction == DirectionEnum.diagonal )
    	{
    		xOffset = 1;
    		yOffset = 1;
    	}
    	for ( int z = 0; z < lenOfWord; z++)
    	{
    		char c = word.charAt(z);
    		boolean insertOk = putCharInField( x, y, c);
    		if (!insertOk)
    		{
    			_violations++;
    		}
    		x = x + xOffset;
    		y = y + yOffset;
    	}
    	letters += word.length();
    	this.violations += _violations;
    	return _violations;
    }
    
    public int countEmptyCells()
    {
    	String s = printToString();
    	
    	int anz = 0;
    	for (int i = 0; i< s.length();i++ )
    	{
    		if (s.charAt(i) == '_')
    		{
    			anz ++;
    		}
    	}
    	return anz;
    }
    
    public boolean isWordUsed( String word)
    {
    	return this.usedWords.contains(word);
    }

	public int getMultiUsedChars() {
		return multiUsedChars;
	}

	public void setMultiUsedChars(int multiUsedChars) {
		this.multiUsedChars = multiUsedChars;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public int getViolations() {
		return violations;
	}

	public void setViolations(int violations) {
		this.violations = violations;
	}

	public int getLetters() {
		return letters;
	}

	public void setLetters(int letters) {
		this.letters = letters;
	}

	public Set<String> getUsedWords() {
		return usedWords;
	}

	public void setUsedWords(Set<String> usedWords) {
		this.usedWords = usedWords;
	}
    
}
