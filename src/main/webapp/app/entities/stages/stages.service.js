(function() {
    'use strict';
    angular
        .module('musixiseApp')
        .factory('Stages', Stages);

    Stages.$inject = ['$resource', 'DateUtils'];

    function Stages ($resource, DateUtils) {
        var resourceUrl =  'api/stages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createtime = DateUtils.convertLocalDateFromServer(data.createtime);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.createtime = DateUtils.convertLocalDateToServer(data.createtime);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.createtime = DateUtils.convertLocalDateToServer(data.createtime);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
