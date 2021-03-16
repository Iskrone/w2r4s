# w2r4s
Want2Read4Spring

**General Info**: <br>
Simple Book Library. 
Test App on Spring Boot + Thymeleaf + H2 DB. 
1. Shows a list of Books from DB 
2. Export to XLSX 
3. Parse directories with books recursively in specified directory.
4. Import from XLSX

**Book pattern**: Author - Name.ext

**Usual scheme of work**:
1. Parse books in directory
2. Export to XLSX
3. Edit XLSX-file. Add, delete, edit books
4. Import to DB
5. Get new XLSX with all changes

**Parse directory features**:<br>
Name, author, extension(format), type - makes book unique.
So you can put books with the same author and name in different directories, according to your ideas.
For example different languages.

You can name files started with "01. ", "02. ",... To keep them sorted in directory.
Parser will cut this numbers away.

**Supported extensions**:<br>
FB2, EPUB, PDF, DJVU, DOC, RTF, TXT, MOBI, PDO, CBR.

**Special extensions**:<br>
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

**Exclude file or directory from parsing**: <br>
Add "-" before file name.

**Mark book to delete in xlsx-file**: <br>
Write "remove" to Note (Примечание) field.
