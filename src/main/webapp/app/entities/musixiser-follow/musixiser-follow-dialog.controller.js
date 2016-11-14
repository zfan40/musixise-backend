(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('MusixiserFollowDialogController', MusixiserFollowDialogController);

    MusixiserFollowDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'MusixiserFollow'];

    function MusixiserFollowDialogController ($scope, $stateParams, $uibModalInstance, entity, MusixiserFollow) {
        var vm = this;
        vm.musixiserFollow = entity;
        vm.load = function(id) {
            MusixiserFollow.get({id : id}, function(result) {
                vm.musixiserFollow = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('musixiseApp:musixiserFollowUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.musixiserFollow.id !== null) {
                MusixiserFollow.update(vm.musixiserFollow, onSaveSuccess, onSaveError);
            } else {
                MusixiserFollow.save(vm.musixiserFollow, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
