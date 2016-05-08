(function() {
    'use strict';
    angular
        .module('musixiseApp')
        .factory('Musixiser', Musixiser);

    Musixiser.$inject = ['$resource'];

    function Musixiser ($resource) {
        var resourceUrl =  'api/musixisers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
