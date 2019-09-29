# 2019_Fall_TCN5030_Project1
## Overview:
Understand TCP socket programing by developing a simplified FTP client. You must create your own socket, and cannot use any existing FTP library. You may use Python, Java, or C++/C as the programming language.
The client should be designed to start by typing the command:

myftp server-name.

where “server-name” is the name or IP address of the server. Next, display a prompt for entering the FTP user name, followed by a prompt for entering the password.
After a successful login, the following commands should be available in the FTP client.

## Cmmand:

 - `MyFTP> ls` - List the files in the current directory on the remote server.
 - `MyFTP> cd remote-dir` - Chane the current directory to “remote-dir” on the remote server.
 - `MyFTP> get remote-file` - Download the file “remote-file” from the remote server to the local machine.
 - `MyFTP> put local-file` - Upload the file “local-file” from the local machine to the remote server.
 - `MyFTP> delete remote-file` - Delete the file “remote-file” from the remote server.
 - `MyFTP> quit` - Quit the FTP client.
 
 All the directory/file names in the commands should be considered relative to the current directory 
 (unless absolute path name is given). All the commands (including entering the user name and password) 
 when executed should return a Success/Failure status. After a successful file transfer, a success message should be displayed 
 with the number of bytes transferred.
 
## Test and Debug:
Please run the Windows/Linux/MacOS built-in command line FTP client to see the expected result of each command. Analyzing the transmitted message using Wireshark will help you understand the implementation.
You may test your client by connecting it to any standard FTP server, such as inet.cis.fiu.edu (user: demo, password: demopass) or the FileZilla server (https://filezilla-project.org/download.php?type=server) that can be installed on your own Windows machine.

## References:
Basic FTP Commands, http://www.cs.colostate.edu/helpdocs/ftp.html

FTP RFC, http://www.ietf.org/rfc/rfc959.txt

## Submission Guide:
Submit the source code file, without any project files.

Also submit a readme.txt file with the following information:

- Student names and IDs:
- Operating system used: Windows/Linux
- Programing language used: Python/Java/C++/C
- Compiling instructions: if any (Python programs need to compiling.), command-line compiling instructions, again no netbeans/eclipse/… project files.
