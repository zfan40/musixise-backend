(function() {
    'use strict';
    angular
        .module('musixiseApp')
        .factory('Audience', Audience);

    Audience.$inject = ['$resource'];

    function Audience ($resource) {
        var resourceUrl =  'api/audiences/:id';

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
