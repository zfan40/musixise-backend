(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .factory('WorkListFollowSearch', WorkListFollowSearch);

    WorkListFollowSearch.$inject = ['$resource'];

    function WorkListFollowSearch($resource) {
        var resourceUrl =  'api/_search/work-list-follows/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
