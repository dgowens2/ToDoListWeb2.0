angular.module('TIYAngularApp', [])
   .controller('SampleController', function($scope, $http) {
        $scope.getTodos = function() {
            console.log("About to go get me some data!");
            $scope.name = "Audrey";

            $http.get("/todos.json")
                .then(
                    function successCallback(response) {
                        console.log(response.data);
                        console.log("Adding data to scope");
                        $scope.todos = response.data;
                    },
                    function errorCallback(response) {
                        console.log("Unable to get data");
                    });

            console.log("Done with the promise - waiting for the callback");

        };

        $scope.toggleTodo = function(todoID) {
            console.log("About to toggle Todo with ID " + todoID);

            $http.get("/toggleTodo.json?todoID=" + todoID)
                .then(
                    function success(response) {
                        console.log(response.data);
                        console.log("ToDo toggled");
                        $scope.todos = response.data;
                    },
                    function error(response) {
                        console.log("Unable to toggle todo");
                    });
        };

        $scope.deleteTodo = function(todoID) {
            console.log("About to delete Todo with ID " + todoID);

            $http.get("/deleteTodo.json?todoID=" + todoID)
//            console.log("In the delete todo function");
                .then(
                    function success(response) {
                    console.log("inside the delete success function")
                        console.log(response.data);
                        console.log("ToDo deleted");
                        $scope.todos = response.data;
                    },
                    function error(response) {
                        console.log("Unable to delete todo");
                    });
        };

        $scope.addTodo = function() {
            console.log("About to add the following todo " + JSON.stringify($scope.newTodo));

            $http.post("/addTodo.json", $scope.newTodo)
                .then(
                    function successCallback(response) {
                        console.log(response.data);
                        console.log("Adding data to scope");
                        $scope.todos = response.data;
                    },
                    function errorCallback(response) {
                        console.log("Unable to get data");
                    });
        };


        $scope.newTodo = {};
    });
