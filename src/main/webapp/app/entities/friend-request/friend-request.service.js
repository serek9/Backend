(function() {
    'use strict';
    angular
        .module('serflixApp')
        .factory('FriendRequest', FriendRequest);

    FriendRequest.$inject = ['$resource', 'DateUtils'];

    function FriendRequest ($resource, DateUtils) {
        var resourceUrl =  'api/friend-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.sentOn = DateUtils.convertDateTimeFromServer(data.sentOn);
                        data.resolvedOn = DateUtils.convertDateTimeFromServer(data.resolvedOn);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
