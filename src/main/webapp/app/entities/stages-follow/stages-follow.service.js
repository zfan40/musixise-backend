(function() {
    'use strict';
    angular
        .module('musixiseApp')
        .factory('StagesFollow', StagesFollow);

    StagesFollow.$inject = ['$resource', 'DateUtils'];

    function StagesFollow ($resource, DateUtils) {
        var resourceUrl =  'api/stages-follows/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.timestamp = DateUtils.convertDateTimeFromServer(data.timestamp);
                    data.updatetime = DateUtils.convertDateTimeFromServer(data.updatetime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
