# Usage

Run the program with:

    java -jar file-counter.jar
    
or by invoking a .sh or .bat file included with the
distribution.

Simply select a directory, edit the excluded files to
your satisfaction, and then press Run.

Note that at present this program will count both
symlinked files and files under symlinked directories
(unlike, say, find <dir> -type f).