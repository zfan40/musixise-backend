(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .factory('StagesFollowSearch', StagesFollowSearch);

    StagesFollowSearch.$inject = ['$resource'];

    function StagesFollowSearch($resource) {
        var resourceUrl =  'api/_search/stages-follows/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
