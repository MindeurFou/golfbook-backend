#!/bin/bash

base_url='http://localhost:8080'

curl "$base_url/player/1" | tee responses_json/player.json

curl "$base_url/course/1" | tee responses_json/courseDetails.json

curl "$base_url/course" | tee responses_json/courseList.json

curl "$base_url/game/1" | tee responses_json/gameDetails.json

curl "$base_url/game/1/scorebook" | tee responses_json/scorebook.json
