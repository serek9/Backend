(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('RequestController', RequestController);

    RequestController.$inject = ['$scope', '$state', 'Request'];

    function RequestController ($scope, $state, Request) {
        var vm = this;

        vm.requests = [];

        loadAll();

        function loadAll() {
            Request.query(function(result) {
                vm.requests = result;
                vm.searchQuery = null;
            });
        }
    }
})();
