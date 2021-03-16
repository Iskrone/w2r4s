# w2r4s
Want2Read4Spring

**General Info**:

Simple Book Library. 
Test App on Spring Boot + Thymeleaf + H2 DB. 
1. Shows a list of Books from DB 
2. Export to XLSX 
3. Parse directories with books recursively in specified directory.
4. Import from XLSX

**Book pattern**: Author - Name.ext

The usual scheme of work:
1. Parse books in directory
2. Export to XLSX
3. Edit XLSX-file. Add, delete, edit books
4. Import to DB
5. Get new XLSX with all changes

**Parse directory features**:<br>
Name, author, extension(format), type - makes book unique.
So you can put books with the same author and name in different directories, according to your ideas.
For example different languages.

You can name files as "01. ", "02. ",... To keep them sorted in directory.
Parser will cut this numbers away.

<ins>Supported extensions</ins>:<br>
FB2, EPUB, PDF, DJVU, DOC, RTF, TXT, MOBI, PDO, CBR.

<ins>Special extensions</ins>:<br>
Parser works with them as with the book. <br>
Just name directory according to Book pattern. <br>
 
_.audio_ - means one audio book inside. 
Parser will look through it. So you can put text-file along with the audio.
<br>
_.comix_ - means comics as a bunch of pictures inside.
Parser will not look through it.
<br>
_.html_ - if it is a directory, then it is html-book consists of bunch of html-files
Parser will not look through it.

<ins>Exclude file or directory from parsing</ins>: <br>
Add "-" before file name.

<ins>Mark book to delete in xlsx-file</ins>: <br>
Write "remove" to Note (Примечание) field.
