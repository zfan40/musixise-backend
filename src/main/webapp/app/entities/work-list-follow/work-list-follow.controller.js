(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('WorkListFollowController', WorkListFollowController);

    WorkListFollowController.$inject = ['$scope', '$state', 'WorkListFollow', 'WorkListFollowSearch'];

    function WorkListFollowController ($scope, $state, WorkListFollow, WorkListFollowSearch) {
        var vm = this;
        vm.workListFollows = [];
        vm.loadAll = function() {
            WorkListFollow.query(function(result) {
                vm.workListFollows = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            WorkListFollowSearch.query({query: vm.searchQuery}, function(result) {
                vm.workListFollows = result;
            });
        };
        vm.loadAll();
        
    }
})();
