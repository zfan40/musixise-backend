(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .factory('StagesSearch', StagesSearch);

    StagesSearch.$inject = ['$resource'];

    function StagesSearch($resource) {
        var resourceUrl =  'api/_search/stages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
