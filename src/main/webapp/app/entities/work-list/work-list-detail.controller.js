(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('WorkListDetailController', WorkListDetailController);

    WorkListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'WorkList'];

    function WorkListDetailController($scope, $rootScope, $stateParams, DataUtils, entity, WorkList) {
        var vm = this;
        vm.workList = entity;
        vm.load = function (id) {
            WorkList.get({id: id}, function(result) {
                vm.workList = result;
            });
        };
        var unsubscribe = $rootScope.$on('musixiseApp:workListUpdate', function(event, result) {
            vm.workList = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
