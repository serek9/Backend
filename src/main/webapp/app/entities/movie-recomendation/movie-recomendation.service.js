(function() {
    'use strict';
    angular
        .module('serflixApp')
        .factory('MovieRecomendation', MovieRecomendation);

    MovieRecomendation.$inject = ['$resource'];

    function MovieRecomendation ($resource) {
        var resourceUrl =  'api/movie-recomendations/:id';

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
