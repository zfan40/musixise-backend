(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('WorkListDialogController', WorkListDialogController);

    WorkListDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'WorkList', 'User'];

    function WorkListDialogController ($scope, $stateParams, $uibModalInstance, DataUtils, entity, WorkList, User) {
        var vm = this;
        vm.workList = entity;
        vm.users = User.query();
        vm.load = function(id) {
            WorkList.get({id : id}, function(result) {
                vm.workList = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('musixiseApp:workListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.workList.id !== null) {
                WorkList.update(vm.workList, onSaveSuccess, onSaveError);
            } else {
                WorkList.save(vm.workList, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.createtime = false;

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
