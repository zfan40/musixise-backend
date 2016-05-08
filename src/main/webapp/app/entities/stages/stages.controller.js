(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('StagesController', StagesController);

    StagesController.$inject = ['$scope', '$state', 'Stages', 'StagesSearch'];

    function StagesController ($scope, $state, Stages, StagesSearch) {
        var vm = this;
        vm.stages = [];
        vm.loadAll = function() {
            Stages.query(function(result) {
                vm.stages = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            StagesSearch.query({query: vm.searchQuery}, function(result) {
                vm.stages = result;
            });
        };
        vm.loadAll();
        
    }
})();
