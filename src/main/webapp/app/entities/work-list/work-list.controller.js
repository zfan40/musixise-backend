(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('WorkListController', WorkListController);

    WorkListController.$inject = ['$scope', '$state', 'DataUtils', 'WorkList', 'WorkListSearch'];

    function WorkListController ($scope, $state, DataUtils, WorkList, WorkListSearch) {
        var vm = this;
        vm.workLists = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.loadAll = function() {
            WorkList.query(function(result) {
                vm.workLists = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            WorkListSearch.query({query: vm.searchQuery}, function(result) {
                vm.workLists = result;
            });
        };
        vm.loadAll();
        
    }
})();
