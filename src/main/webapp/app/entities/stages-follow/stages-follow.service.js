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
                    data.timestamp = DateUtils.convertLocalDateFromServer(data.timestamp);
                    data.updtetime = DateUtils.convertLocalDateFromServer(data.updtetime);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.timestamp = DateUtils.convertLocalDateToServer(data.timestamp);
                    data.updtetime = DateUtils.convertLocalDateToServer(data.updtetime);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.timestamp = DateUtils.convertLocalDateToServer(data.timestamp);
                    data.updtetime = DateUtils.convertLocalDateToServer(data.updtetime);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
