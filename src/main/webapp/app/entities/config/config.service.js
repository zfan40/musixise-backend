(function() {
    'use strict';
    angular
        .module('musixiseApp')
        .factory('Config', Config);

    Config.$inject = ['$resource'];

    function Config ($resource) {
        var resourceUrl =  'api/config-lists/:id';

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
