(function() {
    'use strict';
    angular
        .module('serflixApp')
        .factory('Forecast', Forecast);

    Forecast.$inject = ['$resource'];

    function Forecast ($resource) {
        var resourceUrl =  'api/forecasts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
