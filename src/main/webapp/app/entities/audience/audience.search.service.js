(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .factory('AudienceSearch', AudienceSearch);

    AudienceSearch.$inject = ['$resource'];

    function AudienceSearch($resource) {
        var resourceUrl =  'api/_search/audiences/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
