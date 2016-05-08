(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .factory('MusixiserSearch', MusixiserSearch);

    MusixiserSearch.$inject = ['$resource'];

    function MusixiserSearch($resource) {
        var resourceUrl =  'api/_search/musixisers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
