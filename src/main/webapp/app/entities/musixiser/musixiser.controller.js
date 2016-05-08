(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('MusixiserController', MusixiserController);

    MusixiserController.$inject = ['$scope', '$state', 'Musixiser', 'MusixiserSearch'];

    function MusixiserController ($scope, $state, Musixiser, MusixiserSearch) {
        var vm = this;
        vm.musixisers = [];
        vm.loadAll = function() {
            Musixiser.query(function(result) {
                vm.musixisers = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MusixiserSearch.query({query: vm.searchQuery}, function(result) {
                vm.musixisers = result;
            });
        };
        vm.loadAll();
        
    }
})();
