#! /bin/sh
#
# Usage: ./GETALLsongListTester.sh 4c87dubofnfrheesom6t0833qa mmuster
#
# 4c87dubofnfrheesom6t0833qa = token
# mmuster = userId

if [ "$#" -ne 2 ]; then
    echo "Illegal number of parameters"
    echo "Usage: ./GETALLsongListTester.sh token userId"
    echo "Example: ./GETALLsongListTester.sh 4c87dubofnfrheesom6t0833qa mmuster"
    exit 1
fi

echo "--- TESTING songLists-endpoint WITHOUT TOKEN SHOULD RETURN 401: ------------"
curl -X GET \
     -H "Accept: application/xml" \
     -v "http://localhost:8080/songsRX/rest/songLists"
echo " "
echo "-------------------------------------------------------------------------------------------------"


echo "--- REQUESTING ALL XML SONGLISTs for $2: ------------"
curl -X GET \
     -H "Authorization: $1" \
     -H "Accept: application/xml" \
     -v "http://localhost:8080/songsRX/rest/songLists?userId=$2"
echo " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- REQUESTING ALL JSON SONGLISTs for $2: ------------"
curl -X GET \
     -H "Authorization: $1" \
     -H "Accept: application/json" \
     -v "http://localhost:8080/songsRX/rest/songLists?userId=$2"
echo " "
echo "-------------------------------------------------------------------------------------------------"
