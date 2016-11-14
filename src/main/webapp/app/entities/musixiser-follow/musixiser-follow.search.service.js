(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .factory('MusixiserFollowSearch', MusixiserFollowSearch);

    MusixiserFollowSearch.$inject = ['$resource'];

    function MusixiserFollowSearch($resource) {
        var resourceUrl =  'api/_search/musixiser-follows/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
