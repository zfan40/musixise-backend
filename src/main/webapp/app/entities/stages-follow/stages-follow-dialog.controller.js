(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('StagesFollowDialogController', StagesFollowDialogController);

    StagesFollowDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'StagesFollow'];

    function StagesFollowDialogController ($scope, $stateParams, $uibModalInstance, entity, StagesFollow) {
        var vm = this;
        vm.stagesFollow = entity;
        vm.load = function(id) {
            StagesFollow.get({id : id}, function(result) {
                vm.stagesFollow = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('musixiseApp:stagesFollowUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.stagesFollow.id !== null) {
                StagesFollow.update(vm.stagesFollow, onSaveSuccess, onSaveError);
            } else {
                StagesFollow.save(vm.stagesFollow, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.timestamp = false;
        vm.datePickerOpenStatus.updtetime = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
