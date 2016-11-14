(function() {
    'use strict';
    angular
        .module('musixiseApp')
        .factory('MusixiserFollow', MusixiserFollow);

    MusixiserFollow.$inject = ['$resource'];

    function MusixiserFollow ($resource) {
        var resourceUrl =  'api/musixiser-follows/:id';

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
