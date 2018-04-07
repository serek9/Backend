(function() {
    'use strict';

    angular
        .module('serflixApp')
        .controller('FriendRequestController', FriendRequestController);

    FriendRequestController.$inject = ['$scope', '$state', 'FriendRequest'];

    function FriendRequestController ($scope, $state, FriendRequest) {
        var vm = this;

        vm.friendRequests = [];

        loadAll();

        function loadAll() {
            FriendRequest.query(function(result) {
                vm.friendRequests = result;
                vm.searchQuery = null;
            });
        }
    }
})();
