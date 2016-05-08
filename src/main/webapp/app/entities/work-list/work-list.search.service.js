(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .factory('WorkListSearch', WorkListSearch);

    WorkListSearch.$inject = ['$resource'];

    function WorkListSearch($resource) {
        var resourceUrl =  'api/_search/work-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
