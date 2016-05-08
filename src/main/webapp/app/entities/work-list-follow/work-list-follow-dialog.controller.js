(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('WorkListFollowDialogController', WorkListFollowDialogController);

    WorkListFollowDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'WorkListFollow', 'WorkList', 'User'];

    function WorkListFollowDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, WorkListFollow, WorkList, User) {
        var vm = this;
        vm.workListFollow = entity;
        vm.worklists = WorkList.query();
        vm.users = User.query();
        vm.load = function(id) {
            WorkListFollow.get({id : id}, function(result) {
                vm.workListFollow = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('musixiseApp:workListFollowUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.workListFollow.id !== null) {
                WorkListFollow.update(vm.workListFollow, onSaveSuccess, onSaveError);
            } else {
                WorkListFollow.save(vm.workListFollow, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.createtime = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
