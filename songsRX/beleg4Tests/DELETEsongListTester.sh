#! /bin/sh
#
# Usage: ./DELETEsongListTester.sh 4c87dubofnfrheesom6t0833qa 22
#
# 4c87dubofnfrheesom6t0833qa = token
# 22 = songList
#

if [ "$#" -ne 2 ]; then
    echo "$#"
    echo "Illegal number of parameters"
    echo "Usage: ./DELETEsongListTester.sh token songListId"
    echo "Example: ./DELETEsongListTester.sh 4c87dubofnfrheesom6t0833qa 22"
    exit 1
fi

echo "--- DELETING SONGLIST WITHOUT TOKEN SHOULD RETURN 401--------"
curl -X DELETE \
     -H "Accept: text/plain" \
     -v "http://localhost:8080/songsRX/rest/songLists/$2"
echo " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- DELETING SONGLIST WITH TOKEN--------"
curl -X DELETE \
     -H "Authorization: $1" \
     -H "Accept: text/plain" \
     -v "http://localhost:8080/songsRX/rest/songLists/$2"
echo " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- REQUESTING DELETED SONGLIST SHOULD RETURN 404 --------"
curl -X GET \
     -H "Authorization: $1" \
     -H "Accept: application/json" \
     -v "http://localhost:8080/songsRX/rest/songLists/$2"
echo " "
echo "-------------------------------------------------------------------------------------------------"


