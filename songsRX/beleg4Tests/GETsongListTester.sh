#! /bin/sh
#
# Usage: ./GETsongListTester.sh 4c87dubofnfrheesom6t0833qa 2
#
# 4c87dubofnfrheesom6t0833qa = token
# 2 = songListID
#

if [ "$#" -ne 2 ]; then
    echo "Illegal number of parameters"
    echo "Usage: ./GETsongListTester.sh token songListId"
    echo "Example: ./GETsongListTester.sh 4c87dubofnfrheesom6t0833qa 2"
    exit 1
fi

echo "--- REQUESTING JSON-SONGLIST $3 WITH TOKEN: ------------"
curl -X GET \
     -H "Authorization: $1" \
     -H "Accept: application/json" \
     -v "http://localhost:8080/songsRX/rest/songLists/$2"
echo " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- REQUESTING XML-SONGLIST $3 WITH TOKEN: ------------"
curl -X GET \
     -H "Authorization: $1" \
     -H "Accept: application/xml" \
     -v "http://localhost:8080/songsRX/rest/songLists/$2"
echo " "
echo "-------------------------------------------------------------------------------------------------"
