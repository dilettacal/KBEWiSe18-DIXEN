Vorbereitung: 

Ihre songsRX-Software aus dem git runterladen und installieren

cp -r  beleg4Tests/ songsRX/

Ihre SongLists-Payloads ins Verzeichnis "beleg4Tests/" kopieren und dort meine SongLists-Payloads überschreiben:

cd songsRX/beleg4Tests

cp YOUR_SongList.json aSongList.json

cp YOUR_SongList.xml aSongList.xml

cp YOUR_BadSongList.json aSongListBad.json


Fuer die Praesentation... same procedure as usual:

cd songsRX

git log | less

git status

mvn clean package

deploy songsRX.war in Tomcat

cd beleg4Tests

Alle Skripte in diesem Verzeichnis muessen mit Argumenten aufgerufen werden.
Ein Skript-Aufruf ohne Argument zeigt eine "Usage"-Message an. 
Zum Beispiel:

TOKEN holen fuer einen Ihrer User:
./getToken.sh mmuster

TESTING "/songLists" endpoint:

GET ALL song lists:
./GETALLsongListTester.sh token userIdForToken (mmuster)
./GETALLsongListTester.sh token otherUserId    (eschuler)

GET A song list
./GETsongListTester.sh token publicSongListId       (SongList gehoert mmuster)
./GETsongListTester.sh token privateSongListId      (SongList gehoert mmuster)
./GETsongListTester.sh token non-existing-songListId
./GETsongListTester.sh token publicSongListId  (SongList gehoert dem anderen Nutzer)
./GETsongListTester.sh token privateSongListId (SongList gehoert dem anderen Nutzer)

POST a song list: Sie sollten die folgenden Payloads in diesem Verzeichnis:
aSongList.xml, aSongList.json, aSongListBad.json 
mit Ihren Payloads ueberschreiben. 
POSTsongListTester.sh schickt diese Payloads an Ihren Service.

./POSTsongListTester.sh token

DELETE a song list: 
./DELETEsongListTester.sh token songListId
./DELETEsongListTester.sh token songListId

DELETE a song:
./deleteSong.sh token songId

cd ..
git add beleg4Tests/*
git commit -m "Test results" beleg4Tests/*
git push